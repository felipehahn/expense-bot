package com.financebot.transactionservice.dto;

import com.financebot.transactionservice.enums.TransactionType;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionDTO(Long id, BigDecimal amount, TransactionType type, String description, LocalDate data) {
}
