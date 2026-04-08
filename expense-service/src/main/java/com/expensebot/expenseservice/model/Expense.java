package com.expensebot.expenseservice.model;

import com.expensebot.expenseservice.dto.ExpenseDTO;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "expenses")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

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

    public static Expense create(User user, ExpenseDTO expenseDTO) {
        LocalDateTime now = LocalDateTime.now();

        Expense expense = new Expense();
        expense.setUser(user);
        expense.setDescription(expenseDTO.description());
        expense.setAmount(expenseDTO.amount());
        expense.setData(expenseDTO.data());
        expense.setCreatedAt(now);
        expense.setUpdatedAt(now);
        return expense;
    }
}
