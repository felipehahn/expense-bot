package com.financebot.transactionservice.dto;

import com.financebot.transactionservice.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionDTO(BigDecimal amount, TransactionType type, String description, LocalDate data) {
}
