package com.financebot.transactionservice.repository;

import com.financebot.transactionservice.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByUserId(Long userId);
}
