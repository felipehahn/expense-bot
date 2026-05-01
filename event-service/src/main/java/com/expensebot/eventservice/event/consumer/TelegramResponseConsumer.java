package com.expensebot.eventservice.event.consumer;

import com.expensebot.contracts.event.TelegramResponseEvent;
import com.expensebot.eventservice.client.TelegramClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

public class TelegramResponseConsumer {
    @Autowired
    private TelegramClient telegramClient;

    @KafkaListener(topics = "telegram-responses")
    public void consume(TelegramResponseEvent event) {
        telegramClient.sendMessage(event.chatId(), event.text());
    }
}
