package com.financebot.transactionservice.event.contract;

import com.financebot.contracts.event.TelegramCommandEvent;
import com.financebot.transactionservice.session.UserSession;

public interface EventHandler {
    String getCommand();
    void process(TelegramCommandEvent event, UserSession session);
}
