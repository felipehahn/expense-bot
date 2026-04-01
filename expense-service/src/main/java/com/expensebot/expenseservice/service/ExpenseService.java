package com.expensebot.expenseservice.service;

import com.expensebot.expenseservice.dto.ExpenseDTO;
import com.expensebot.expenseservice.model.Expense;
import com.expensebot.expenseservice.model.User;
import com.expensebot.expenseservice.repository.ExpenseRepository;
import com.expensebot.expenseservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpenseService {

    @Autowired
    ExpenseRepository expenseRepository;

    @Autowired
    UserRepository userRepository;

    public Expense create(Long telegramUserId, ExpenseDTO expenseCommand) {
        User user = userRepository.findByTelegramId(telegramUserId).orElseThrow();
        return expenseRepository.save(Expense.create(user, expenseCommand.description(), expenseCommand.amount()));
    }

    public void delete(Long expenseId) {
        expenseRepository.deleteById(expenseId);
    }
}
