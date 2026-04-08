package com.expensebot.expenseservice.event.handler;

import com.expensebot.contracts.event.TelegramCommandEvent;
import com.expensebot.expenseservice.event.contract.EventHandler;
import com.expensebot.expenseservice.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteExpenseEventHandler implements EventHandler {

    @Autowired
    ExpenseService expenseService;

    @Override
    public String getCommand() {
        return "/deldespesa";
    }

    @Override
    public void process(TelegramCommandEvent event) {
        Long expenseId = parse(event.text());
        expenseService.delete(expenseId);
    }

    private Long parse(String text) {
        String[] parts = text.split(" ", 1);

        if (parts.length != 1)
            throw new IllegalArgumentException("Formato inválido. Use: /despesa <valor> <descrição>");

        return Long.parseLong(parts[0]);
    }
}
