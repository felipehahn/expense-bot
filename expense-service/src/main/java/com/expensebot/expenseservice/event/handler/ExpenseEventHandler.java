package com.expensebot.expenseservice.event.handler;

import com.expensebot.contracts.event.TelegramCommandEvent;
import com.expensebot.expenseservice.event.contract.EventHandler;
import com.expensebot.expenseservice.dto.ExpenseDTO;
import com.expensebot.expenseservice.event.publisher.TelegramEventPublisher;
import com.expensebot.expenseservice.exception.BotException;
import com.expensebot.expenseservice.service.ExpenseService;
import com.expensebot.expenseservice.session.UserSession;
import com.expensebot.expenseservice.session.UserSessionRepository;
import com.expensebot.expenseservice.session.UserSessionStep;
import com.expensebot.expenseservice.utils.KeyboardBuilder;
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
public class ExpenseEventHandler implements EventHandler {

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
            "Mercado", "Farmácia", "Restaurante", "Transporte",
            "Lazer", "Saúde", "Educação", "Outro"
    );

    @Autowired
    ExpenseService expenseService;

    @Autowired
    UserSessionRepository userSessionRepository;

    @Autowired
    TelegramEventPublisher publisher;

    @Override
    public String getCommand() {
        return "/despesa";
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
        publisher.send(event.chatId(), "Informe o valor de seu gasto:");
    }

    private void handleAmount(TelegramCommandEvent event, UserSession session) {
        session.getData().put("amount", parseAmount(event.text()));
        session.setStep(UserSessionStep.WAITING_DESCRIPTION);
        userSessionRepository.save(event.userId(), session);
        publisher.send(event.chatId(), "Informe uma descrição para o seu gasto:", KeyboardBuilder.build(DEFAULT_DESCRIPTIONS));
    }

    private void handleDescription(TelegramCommandEvent event, UserSession session) {
        session.getData().put("description", event.text().trim());
        session.setStep(UserSessionStep.WAITING_DATE);
        userSessionRepository.save(event.userId(), session);
        publisher.send(event.chatId(), "Informe uma data para seu gasto(dd/MM/yyyy):",  KeyboardBuilder.build(DEFAULT_DATES));
    }

    private void handleDate(TelegramCommandEvent event, UserSession session) {
        LocalDate date = parseData(event.text().trim());

        expenseService.create(event.userId(), new ExpenseDTO(
                (BigDecimal) session.getData().get("amount"),
                (String) session.getData().get("description"),
                date
        ));
        userSessionRepository.remove(event.userId());
        publisher.send(event.chatId(), "✅ Despesa registrada!");
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
