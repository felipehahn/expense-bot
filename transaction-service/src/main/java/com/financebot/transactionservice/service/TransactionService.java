package com.financebot.transactionservice.service;

import com.financebot.transactionservice.dto.TransactionDTO;
import com.financebot.transactionservice.model.Transaction;
import com.financebot.transactionservice.model.User;
import com.financebot.transactionservice.repository.TransactionRepository;
import com.financebot.transactionservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserRepository userRepository;

    public Transaction create(Long telegramUserId, TransactionDTO transactionDTO) {
        User user = userRepository.findByTelegramId(telegramUserId).orElseThrow();
        return transactionRepository.save(Transaction.create(user, transactionDTO));
    }

    public void delete(Long expenseId) {
        transactionRepository.deleteById(expenseId);
    }
}
