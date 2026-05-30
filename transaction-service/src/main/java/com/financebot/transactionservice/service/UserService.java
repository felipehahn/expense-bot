package com.financebot.transactionservice.service;

import com.financebot.transactionservice.model.User;
import com.financebot.transactionservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository repository;

    public void createIfNotExists(Long telegramId) {
        repository.findByTelegramId(telegramId)
                .orElseGet(() -> repository.save(User.create(telegramId)));
    }
}
