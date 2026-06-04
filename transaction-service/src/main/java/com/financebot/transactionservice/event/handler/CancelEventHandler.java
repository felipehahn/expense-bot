package com.financebot.transactionservice.event.handler;

import com.financebot.contracts.event.TelegramCommandEvent;
import com.financebot.transactionservice.event.contract.EventHandler;
import com.financebot.transactionservice.event.publisher.TelegramEventPublisher;
import com.financebot.transactionservice.session.UserSession;
import com.financebot.transactionservice.session.UserSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CancelEventHandler implements EventHandler {

    @Autowired
    private UserSessionRepository userSessionRepository;

    @Autowired
    private TelegramEventPublisher publisher;

    @Override
    public String getCommand() {
        return "/cancelar";
    }

    @Override
    public void process(TelegramCommandEvent event, UserSession session) {
        userSessionRepository.remove(event.userId());
        publisher.send(event.chatId(), "Ação cancelada com sucesso.");
    }
}
