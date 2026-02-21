package com.expensebot.eventservice.service;

import com.expensebot.eventservice.dto.event.TelegramCommandEvent;
import com.expensebot.eventservice.dto.telegram.TelegramMessageDTO;
import com.expensebot.eventservice.dto.telegram.TelegramUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TelegramWebhookService {

    @Autowired
    KafkaTemplate<String, TelegramCommandEvent> kafkaTemplate;

    private static final String TOPIC_TELEGRAM_COMMAND = "expense-events";

    public void process(TelegramUpdateDTO update) {

        if (update.getMessage() == null || update.getMessage().getText() == null)
            return;

        String text = update.getMessage().getText();

        if (text.startsWith("/")) publishCommandEvent(update);
    }

    private void publishCommandEvent(TelegramUpdateDTO update) {
        TelegramMessageDTO message = update.getMessage();

        TelegramCommandEvent event = TelegramCommandEvent.builder()
                .chatId(message.getChat().getId())
                .userId(message.getFrom().getId())
                .messageId(message.getMessageId())
                .text(message.getText())
                .command(message.getText().split(" ")[0])
                .build();

        kafkaTemplate.send(TOPIC_TELEGRAM_COMMAND, event);
    }
}
