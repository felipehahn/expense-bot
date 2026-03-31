package com.expensebot.expenseservice.service;

import com.expensebot.expenseservice.model.Expense;
import com.expensebot.expenseservice.model.User;
import com.expensebot.expenseservice.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpenseService {

    @Autowired
    ExpenseRepository repository;

    public Expense create(User user, String description, Double amount) {
        return repository.save(Expense.create(user, description, amount));
    }
}
