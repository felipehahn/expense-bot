package com.financebot.transactionservice.event.handler;

import com.financebot.contracts.event.TelegramCommandEvent;
import com.financebot.transactionservice.dto.TransactionDTO;
import com.financebot.transactionservice.enums.ReportPeriod;
import com.financebot.transactionservice.enums.ReportType;
import com.financebot.transactionservice.enums.TransactionType;
import com.financebot.transactionservice.event.contract.EventHandler;
import com.financebot.transactionservice.event.publisher.TelegramEventPublisher;
import com.financebot.transactionservice.exception.BotException;
import com.financebot.transactionservice.service.TransactionService;
import com.financebot.transactionservice.session.UserSession;
import com.financebot.transactionservice.session.UserSessionRepository;
import com.financebot.transactionservice.session.UserSessionStep;
import com.financebot.transactionservice.utils.InlineKeyboardBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@Service
public class ReportEventHandler implements EventHandler {
    private final Map<UserSessionStep, BiConsumer<TelegramCommandEvent, UserSession>> stepHandlers = Map.of(
            UserSessionStep.INITIAL_STEP, this::handleInit,
            UserSessionStep.WAITING_REPORT_TYPE, this::handleType,
            UserSessionStep.WAITING_REPORT_PERIOD, this::handlePeriod,
            UserSessionStep.WAITING_REPORT_DATE, this::handleDate
    );

    private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("#,##0.00");

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserSessionRepository userSessionRepository;

    @Autowired
    private TelegramEventPublisher publisher;

    @Override
    public String getCommand() {
        return "/relatorio";
    }

    @Override
    public void process(TelegramCommandEvent event, UserSession session) {
        if (session == null)
            session = new UserSession(this.getCommand(), UserSessionStep.INITIAL_STEP);

        stepHandlers.get(session.getStep()).accept(event, session);
    }

    private void handleInit(TelegramCommandEvent event, UserSession session) {
        session.setStep(UserSessionStep.WAITING_REPORT_TYPE);
        userSessionRepository.save(event.userId(), session);

        publisher.send(event.chatId(), "Selecione o tipo do relatório:",
                InlineKeyboardBuilder.build(List.of(
                        List.of(
                                InlineKeyboardBuilder.button("Despesas", ReportType.EXPENSE.toString()),
                                InlineKeyboardBuilder.button("Receitas", ReportType.INCOME.toString()),
                                InlineKeyboardBuilder.button("Ambas", ReportType.ALL.toString())
                        )
                ))
        );
    }

    private void handleType(TelegramCommandEvent event, UserSession session) {
        ReportType reportType = ReportType.fromCallback(event.text());
        session.getData().put("type", reportType);
        session.setStep(UserSessionStep.WAITING_REPORT_PERIOD);
        userSessionRepository.save(event.userId(), session);

        publisher.send(event.chatId(), "Selecione o período:",
                InlineKeyboardBuilder.build(List.of(
                        List.of(
                                InlineKeyboardBuilder.button("Mês atual", ReportPeriod.CURRENT.toString()),
                                InlineKeyboardBuilder.button("Outro período", ReportPeriod.CUSTOM.toString())
                        )
                ))
        );
    }

    private void handlePeriod(TelegramCommandEvent event, UserSession session) {
        ReportPeriod period = ReportPeriod.fromCallback(event.text());

        if (period == ReportPeriod.CURRENT) {
            generateReport(event, session, YearMonth.now());
            return;
        }

        session.setStep(UserSessionStep.WAITING_REPORT_DATE);
        userSessionRepository.save(event.userId(), session);
        publisher.send(event.chatId(), "Informe o período (MM/yyyy):");
    }

    private void handleDate(TelegramCommandEvent event, UserSession session) {
        YearMonth yearMonth = parseYearMonth(event.text());
        generateReport(event, session, yearMonth);
    }

    private void generateReport(TelegramCommandEvent event, UserSession session, YearMonth yearMonth) {
        try {
            ReportType reportType = (ReportType) session.getData().get("type");
            TransactionType transactionType =
                    reportType == ReportType.INCOME ? TransactionType.INCOME
                            :  reportType == ReportType.EXPENSE ? TransactionType.EXPENSE : null;

            List<TransactionDTO> transactions = transactionService.findByUserAndPeriod(
                    event.userId(), yearMonth, transactionType
            );

            if (transactions.isEmpty()) {
                publisher.send(event.chatId(), "Nenhuma transação encontrada para o período.");
                return;
            }

            publisher.send(event.chatId(), buildMessage(transactions, yearMonth, reportType));
        } finally {
            userSessionRepository.remove(event.userId());
        }
    }

    private String buildMessage(List<TransactionDTO> transactions, YearMonth yearMonth, ReportType reportType
    ) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter periodFormatter = DateTimeFormatter.ofPattern("MM/yyyy");

        String titulo = switch (reportType) {
            case EXPENSE -> "Relatório de Despesas";
            case INCOME -> "Relatório de Receitas";
            case ALL -> "Relatório de Receitas e Despesas";
        };

        StringBuilder sb = new StringBuilder();

        sb.append("📊 *")
                .append(titulo)
                .append(" - ")
                .append(yearMonth.format(periodFormatter))
                .append("*\n\n");

        for (TransactionDTO t : transactions) {
            if (reportType == ReportType.ALL) {
                String icon = t.type() == TransactionType.INCOME
                        ? "💰 Receita"
                        : "💸 Despesa";

                sb.append("# Código: ")
                        .append(t.id())
                        .append(" ")
                        .append(icon)
                        .append("\n");
            } else {
                sb.append("# Código: ")
                        .append(t.id())
                        .append("\n");
            }

            sb.append(t.data().format(dateFormatter))
                    .append(" — ")
                    .append(t.description())
                    .append(": R$ ")
                    .append(CURRENCY_FORMAT.format(t.amount()))
                    .append("\n\n");
        }

        BigDecimal total;

        switch (reportType) {
            case EXPENSE -> {
                total = transactions.stream()
                        .map(TransactionDTO::amount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                sb.append("\n *Total de despesas: R$ ")
                        .append(CURRENCY_FORMAT.format(total))
                        .append("*");
            }

            case INCOME -> {
                total = transactions.stream()
                        .map(TransactionDTO::amount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                sb.append("\n *Total de receitas: R$ ")
                        .append(CURRENCY_FORMAT.format(total))
                        .append("*");
            }

            case ALL -> {
                total = transactions.stream()
                        .map(t -> t.type() == TransactionType.INCOME
                                ? t.amount()
                                : t.amount().negate())
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                sb.append("\n📈 *Saldo: R$ ")
                        .append(CURRENCY_FORMAT.format(total))
                        .append("*");
            }
        }

        return sb.toString();
    }

    private YearMonth parseYearMonth(String text) {
        try {
            return YearMonth.parse(text.trim(), DateTimeFormatter.ofPattern("MM/yyyy"));
        } catch (Exception e) {
            throw new BotException("Período inválido: " + text + ". Use o formato MM/yyyy.");
        }
    }
}
