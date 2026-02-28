package com.expensebot.eventservice.service;

import com.expensebot.contracts.event.TelegramCommandEvent;
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

        TelegramCommandEvent event = new TelegramCommandEvent(
                message.getChat().getId(),
                message.getFrom().getId(),
                buildUserName(message),
                message.getText().split(" ")[0],
                message.getText(),
                message.getMessageId()
        );

        kafkaTemplate.send(TOPIC_TELEGRAM_COMMAND, event);
    }

    private String buildUserName(TelegramMessageDTO message) {
        String firstName = message.getFrom().getFirstName();
        String lastName = message.getFrom().getLastName();

        String fullName = (firstName == null ? "" : firstName) + " " + (lastName == null ? "" : lastName);
        String normalized = fullName.trim();

        return normalized.isEmpty() ? "Unknown" : normalized;
    }
}
