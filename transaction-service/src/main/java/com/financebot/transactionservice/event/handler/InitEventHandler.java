package com.financebot.transactionservice.event.handler;

import com.financebot.contracts.event.TelegramCommandEvent;
import com.financebot.transactionservice.event.contract.EventHandler;
import com.financebot.transactionservice.service.UserService;
import com.financebot.transactionservice.session.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InitEventHandler implements EventHandler {

    @Autowired
    private UserService userService;

    @Override
    public String getCommand() {
        return "/start";
    }

    @Override
    public void process(TelegramCommandEvent event, UserSession session) {
        userService.createIfNotExists(event.userId());
    }
}
