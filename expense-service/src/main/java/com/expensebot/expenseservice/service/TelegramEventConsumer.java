package com.expensebot.expenseservice.service;

import com.expensebot.contracts.event.TelegramCommandEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TelegramEventConsumer {

    @Autowired
    private UserService userService;

    @KafkaListener(topics = "expense-events")
    public void consume(TelegramCommandEvent event) {
        System.out.println("Novo evento de gasto chegou!");
        System.out.println(event);

        if ("/start".equals(event.command())) {
            userService.createIfNotExists(event.userId(), event.userName());
        }
    }
}
