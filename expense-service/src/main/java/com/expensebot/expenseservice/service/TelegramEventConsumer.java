package com.expensebot.expenseservice.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TelegramEventConsumer {

    @KafkaListener(topics = "expense-events")
    public void consume(Object event) {
        System.out.println("Novo evento de gasto chegou!");
        System.out.println(event);
    }
}
