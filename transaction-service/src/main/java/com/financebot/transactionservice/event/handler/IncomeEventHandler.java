package com.financebot.transactionservice.event.handler;

import com.financebot.contracts.event.TelegramCommandEvent;
import com.financebot.contracts.event.TelegramReplyKeyboardRemove;
import com.financebot.transactionservice.dto.TransactionDTO;
import com.financebot.transactionservice.enums.TransactionType;
import com.financebot.transactionservice.event.contract.EventHandler;
import com.financebot.transactionservice.event.publisher.TelegramEventPublisher;
import com.financebot.transactionservice.exception.BotException;
import com.financebot.transactionservice.service.TransactionService;
import com.financebot.transactionservice.session.UserSession;
import com.financebot.transactionservice.session.UserSessionRepository;
import com.financebot.transactionservice.session.UserSessionStep;
import com.financebot.transactionservice.utils.KeyboardBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@Service
public class IncomeEventHandler implements EventHandler {
    private final Map<UserSessionStep, BiConsumer<TelegramCommandEvent, UserSession>> stepHandlers = Map.of(
            UserSessionStep.INITIAL_STEP, this::handleInit,
            UserSessionStep.WAITING_AMOUNT, this::handleAmount,
            UserSessionStep.WAITING_DESCRIPTION, this::handleDescription,
            UserSessionStep.WAITING_DATE, this::handleDate
    );

    private static final List<String> DEFAULT_DATES = List.of(
            "Hoje", "Ontem", "Anteontem"
    );

    private static final List<String> DEFAULT_DESCRIPTIONS = List.of(
            "Salário", "Rendimentos", "Outro"
    );

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserSessionRepository userSessionRepository;

    @Autowired
    private TelegramEventPublisher publisher;

    @Override
    public String getCommand() {
        return "/receita";
    }

    @Override
    public void process(TelegramCommandEvent event, UserSession session) {
        if (session == null)
            session = new UserSession(this.getCommand(), UserSessionStep.INITIAL_STEP);

        stepHandlers.get(session.getStep()).accept(event, session);
    }

    private void handleInit(TelegramCommandEvent event, UserSession session) {
        session.setStep(UserSessionStep.WAITING_AMOUNT);
        userSessionRepository.save(event.userId(), session);
        publisher.send(event.chatId(), "Informe o valor de sua receita:");
    }

    private void handleAmount(TelegramCommandEvent event, UserSession session) {
        session.getData().put("amount", parseAmount(event.text()));
        session.setStep(UserSessionStep.WAITING_DESCRIPTION);
        userSessionRepository.save(event.userId(), session);
        publisher.send(event.chatId(), "Informe uma descrição para sua receita (até 200 caracteres)." +
                "\nEscolha uma categoria abaixo ou digite uma específica:", KeyboardBuilder.build(DEFAULT_DESCRIPTIONS));
    }

    private void handleDescription(TelegramCommandEvent event, UserSession session) {
        String description = event.text().trim();
        if (description.length() > 200)
            throw new BotException("A descrição do gasto deve ter no máximo 200 caracteres");

        session.getData().put("description", description);
        session.setStep(UserSessionStep.WAITING_DATE);
        userSessionRepository.save(event.userId(), session);
        publisher.send(event.chatId(), "Informe a data da receita no formato dd/MM/yyyy." +
                "\nEscolha uma opção abaixo ou digite a data:",  KeyboardBuilder.build(DEFAULT_DATES));
    }

    private void handleDate(TelegramCommandEvent event, UserSession session) {
        LocalDate date = parseData(event.text().trim());

        transactionService.create(event.userId(), new TransactionDTO(
                null,
                (BigDecimal) session.getData().get("amount"),
                TransactionType.INCOME,
                (String) session.getData().get("description"),
                date
        ));
        userSessionRepository.remove(event.userId());
        publisher.send(event.chatId(), "✅ Receita registrada!", new TelegramReplyKeyboardRemove());
    }

    private BigDecimal parseAmount(String amountString) {
        try {
            String normalized = amountString.trim()
                    .replace(",", ".");
            return new BigDecimal(normalized);
        } catch (Exception e) {
            throw new BotException("Valor inválido: " + amountString);
        }
    }

    private LocalDate parseData(String dateString) {
        try {
            if (dateString == null || dateString.isBlank() || dateString.equalsIgnoreCase("hoje"))
                return LocalDate.now();

            if (dateString.equalsIgnoreCase("ontem"))
                return LocalDate.now().minusDays(1);

            if (dateString.equalsIgnoreCase("anteontem"))
                return LocalDate.now().minusDays(2);

            return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (DateTimeParseException e) {
            throw new BotException("Data inválida: " + dateString + ". Use o formato dd/MM/yyyy");
        }
    }
}
