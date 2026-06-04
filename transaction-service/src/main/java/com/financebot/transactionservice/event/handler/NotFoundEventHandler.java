package com.financebot.transactionservice.event.handler;

import com.financebot.contracts.event.TelegramCommandEvent;
import com.financebot.transactionservice.event.contract.EventHandler;
import com.financebot.transactionservice.event.publisher.TelegramEventPublisher;
import com.financebot.transactionservice.session.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotFoundEventHandler implements EventHandler {

    @Autowired
    private TelegramEventPublisher publisher;

    @Override
    public String getCommand() {
        return "/default";
    }

    @Override
    public void process(TelegramCommandEvent event, UserSession session) {
        publisher.send(event.chatId(), "Comando não reconhecido. Use /ajuda para ver os comandos disponíveis.");
    }
}
