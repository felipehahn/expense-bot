package com.financebot.transactionservice.event.handler;

import com.financebot.contracts.event.TelegramCommandEvent;
import com.financebot.transactionservice.event.contract.EventHandler;
import com.financebot.transactionservice.service.TransactionService;
import com.financebot.transactionservice.session.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteExpenseEventHandler implements EventHandler {

    @Autowired
    TransactionService transactionService;

    @Override
    public String getCommand() {
        return "/deldespesa";
    }

    @Override
    public void process(TelegramCommandEvent event, UserSession session) {
        Long expenseId = parse(event.text());
        transactionService.delete(expenseId);
    }

    private Long parse(String text) {
        String[] parts = text.split(" ", 1);

        if (parts.length != 1)
            throw new IllegalArgumentException("Formato inválido. Use: /despesa <valor> <descrição>");

        return Long.parseLong(parts[0]);
    }
}
