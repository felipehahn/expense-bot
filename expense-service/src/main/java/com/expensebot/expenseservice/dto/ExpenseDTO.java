package com.expensebot.expenseservice.dto;

import java.math.BigDecimal;

public record ExpenseDTO(BigDecimal amount, String description) {
}
