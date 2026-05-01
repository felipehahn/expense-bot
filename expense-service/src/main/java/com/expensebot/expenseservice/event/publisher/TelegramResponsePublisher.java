package com.expensebot.expenseservice.event.publisher;

import com.expensebot.contracts.event.TelegramResponseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TelegramResponsePublisher {
    private static final String TOPIC = "telegram-responses";

    @Autowired
    private KafkaTemplate<String, TelegramResponseEvent> kafkaTemplate;

    public void send(Long chatId, String text) {
        kafkaTemplate.send(TOPIC, new TelegramResponseEvent(chatId, text));
    }
}
