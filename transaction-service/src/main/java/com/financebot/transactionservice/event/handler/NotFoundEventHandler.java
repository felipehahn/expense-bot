package com.financebot.transactionservice.event.handler;

import com.financebot.contracts.event.TelegramCommandEvent;
import com.financebot.transactionservice.event.contract.EventHandler;
import com.financebot.transactionservice.session.UserSession;
import org.springframework.stereotype.Service;

@Service
public class NotFoundEventHandler implements EventHandler {

    @Override
    public String getCommand() {
        return "/default";
    }

    @Override
    public void process(TelegramCommandEvent event, UserSession session) {

    }
}
