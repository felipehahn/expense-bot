package com.expensebot.expenseservice.event.handler;

import com.expensebot.contracts.event.TelegramCommandEvent;
import com.expensebot.expenseservice.event.contract.EventHandler;
import com.expensebot.expenseservice.dto.ExpenseDTO;
import com.expensebot.expenseservice.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ExpenseEventHandler implements EventHandler {

    @Autowired
    ExpenseService expenseService;

    @Override
    public String getCommand() {
        return "/despesa";
    }

    @Override
    public void process(TelegramCommandEvent event) {
        ExpenseDTO expenseCommand = parse(event.text());
        expenseService.create(event.userId(), expenseCommand);
    }

    private ExpenseDTO parse(String text) {
        String[] parts = text.split(" ", 2);

        if (parts.length < 2)
            throw new IllegalArgumentException("Formato inválido. Use: /despesa <valor> <descrição>");

        BigDecimal amount = parseAmount(parts[0]);
        String description = parts[1];

        return new ExpenseDTO(amount, description);
    }

    private BigDecimal parseAmount(String amountString) {
        try {
            String normalized = amountString
                    .replace(",", ".");
            return new BigDecimal(normalized);
        } catch (Exception e) {
            throw new IllegalArgumentException("Valor inválido: " + amountString);
        }
    }
}
