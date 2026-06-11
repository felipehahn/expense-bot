package com.financebot.transactionservice.repository;

import com.financebot.transactionservice.dto.TransactionDTO;
import com.financebot.transactionservice.enums.TransactionType;
import com.financebot.transactionservice.model.Transaction;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByUserId(Long userId);

    boolean existsByIdAndUserTelegramId(Long id, Long telegramId);

    @Query("""
            SELECT new com.financebot.transactionservice.dto.TransactionDTO(
                t.id,
                t.amount,
                t.type,
                t.description,
                t.data
            )
            FROM Transaction t
            WHERE t.user.telegramId = :userId
              AND t.data >= :startDate
              AND t.data <= :endDate
              AND (:type IS NULL OR t.type = :type)
            ORDER BY t.data ASC
""")
    List<TransactionDTO> listTransactionsPeriod(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("type") TransactionType type
    );
}
