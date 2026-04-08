package com.expensebot.expenseservice.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseDTO(BigDecimal amount, String description, LocalDate data) {
}
