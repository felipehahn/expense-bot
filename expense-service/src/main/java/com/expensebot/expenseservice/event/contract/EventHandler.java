package com.expensebot.expenseservice.event.contract;

import com.expensebot.contracts.event.TelegramCommandEvent;
import com.expensebot.expenseservice.session.UserSession;

public interface EventHandler {
    String getCommand();
    void process(TelegramCommandEvent event, UserSession session);
}
