package com.financebot.transactionservice.event.publisher;

import com.financebot.contracts.event.TelegramInlineKeyboardMarkup;
import com.financebot.contracts.event.TelegramReplyKeyboardMarkup;
import com.financebot.contracts.event.TelegramResponseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TelegramEventPublisher {
    private static final String TOPIC = "telegram-responses";

    @Autowired
    private KafkaTemplate<String, TelegramResponseEvent> kafkaTemplate;

    public void send(Long chatId, String text) {
        kafkaTemplate.send(TOPIC, new TelegramResponseEvent(chatId, text, null));
    }

    public void send(Long chatId, String text, Object replyMarkup) {
        kafkaTemplate.send(TOPIC, new TelegramResponseEvent(chatId, text, replyMarkup));
    }
}
