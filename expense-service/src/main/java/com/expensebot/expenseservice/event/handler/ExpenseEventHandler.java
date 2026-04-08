package com.expensebot.expenseservice.event.handler;

import com.expensebot.contracts.event.TelegramCommandEvent;
import com.expensebot.expenseservice.event.contract.EventHandler;
import com.expensebot.expenseservice.dto.ExpenseDTO;
import com.expensebot.expenseservice.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
        ExpenseDTO expenseDTO = parse(event.text());
        expenseService.create(event.userId(), expenseDTO);
    }

    private ExpenseDTO parse(String text) {
        String[] parts = text.split("\\|", 3);

        if (parts.length < 2)
            throw new IllegalArgumentException("Formato inválido. Use: /despesa <valor> <descrição>");

        BigDecimal amount = parseAmount(parts[0].trim());
        String description = parts[1].trim();
        LocalDate dataDespesa = parseData(parts.length > 2 ? parts[2].trim() : null);

        return new ExpenseDTO(amount, description, dataDespesa);
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

    private LocalDate parseData(String dateString) {
        try {
            if (dateString == null || dateString.isBlank())
                return LocalDate.now();

            return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Data inválida: " + dateString + ". Use o formato dd/MM/yyyy");
        }
    }
}
