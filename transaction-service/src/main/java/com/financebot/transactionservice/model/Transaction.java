package com.financebot.transactionservice.model;

import com.financebot.transactionservice.dto.TransactionDTO;
import com.financebot.transactionservice.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private String description;

    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public static Transaction create(User user, TransactionDTO transactionDTO) {
        LocalDateTime now = LocalDateTime.now();

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setType(transactionDTO.type());
        transaction.setDescription(transactionDTO.description());
        transaction.setAmount(transactionDTO.amount());
        transaction.setData(transactionDTO.data());
        transaction.setCreatedAt(now);
        transaction.setUpdatedAt(now);
        return transaction;
    }
}
