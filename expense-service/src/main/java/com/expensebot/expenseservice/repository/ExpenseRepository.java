package com.expensebot.expenseservice.repository;

import com.expensebot.expenseservice.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExpenseRepository  extends JpaRepository<Expense, Long> {
    Optional<Expense> findByUserId(Long userId);
}
