package com.expensebot.expenseservice.service;

import com.expensebot.expenseservice.model.User;
import com.expensebot.expenseservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository repository;

    public void createIfNotExists(Long telegramId, String name) {
        repository.findByTelegramId(telegramId)
                .orElseGet(() -> repository.save(User.create(telegramId, name)));
    }
}
