package com.expensebot.expenseservice.event.handler;

import com.expensebot.contracts.event.TelegramCommandEvent;
import com.expensebot.expenseservice.event.contract.EventHandler;
import org.springframework.stereotype.Service;

@Service
public class NotFoundEventHandler implements EventHandler {

    @Override
    public String getCommand() {
        return "/default";
    }

    @Override
    public void process(TelegramCommandEvent event) {

    }
}
