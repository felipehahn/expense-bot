package com.expensebot.expenseservice.event.contract;

import com.expensebot.contracts.event.TelegramCommandEvent;

public interface EventHandler {
    String getCommand();
    void process(TelegramCommandEvent event);
}
