package com.financebot.transactionservice.event.handler;

import com.financebot.contracts.event.TelegramCommandEvent;
import com.financebot.transactionservice.event.contract.EventHandler;
import com.financebot.transactionservice.event.publisher.TelegramEventPublisher;
import com.financebot.transactionservice.exception.BotException;
import com.financebot.transactionservice.service.TransactionService;
import com.financebot.transactionservice.session.UserSession;
import com.financebot.transactionservice.session.UserSessionRepository;
import com.financebot.transactionservice.session.UserSessionStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.BiConsumer;

@Service
public class DeleteTransactionEventHandler implements EventHandler {

    private final Map<UserSessionStep, BiConsumer<TelegramCommandEvent, UserSession>> stepHandlers = Map.of(
            UserSessionStep.INITIAL_STEP, this::handleInit,
            UserSessionStep.WAITING_ID, this::handleId
    );

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserSessionRepository userSessionRepository;

    @Autowired
    private TelegramEventPublisher publisher;

    @Override
    public String getCommand() {
        return "/deltransacao";
    }

    @Override
    public void process(TelegramCommandEvent event, UserSession session) {
        if (session == null)
            session = new UserSession(this.getCommand(), UserSessionStep.INITIAL_STEP);

        stepHandlers.get(session.getStep()).accept(event, session);
    }

    private void handleInit(TelegramCommandEvent event, UserSession session) {
        session.setStep(UserSessionStep.WAITING_ID);
        userSessionRepository.save(event.userId(), session);
        publisher.send(event.chatId(), "Informe o código da transação:");
    }

    private void handleId(TelegramCommandEvent event, UserSession session) {
        Long id = parseId(event.text());
        transactionService.delete(id, event.userId());
        userSessionRepository.remove(event.userId());
        publisher.send(event.chatId(), "✅ Transação excluída com sucesso!");
    }

    private Long parseId(String idString) {
        try {
            return Long.parseLong(idString.trim());
        } catch (Exception e) {
            throw new BotException("Código inválido: " + idString + ". Informe apenas números inteiros.");
        }
    }
}
