package com.expensebot.eventservice.service;

import com.expensebot.contracts.event.TelegramCommandEvent;
import com.expensebot.eventservice.dto.telegram.TelegramMessageDTO;
import com.expensebot.eventservice.dto.telegram.TelegramUpdateDTO;
import com.expensebot.eventservice.event.publisher.TelegramEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TelegramWebhookService {

    @Autowired
    TelegramEventPublisher publisher;

    public void process(TelegramUpdateDTO update) {
        if (update.getMessage() == null || update.getMessage().getText() == null)
            return;

        publishCommandEvent(update);
    }

    private void publishCommandEvent(TelegramUpdateDTO update) {
        TelegramMessageDTO message = update.getMessage();
        String text = message.getText();

        String[] messageTextParts = text.startsWith("/") ?
                text.split(" ", 2)
                : new String[]{null, text};

        String command = messageTextParts[0];
        text = messageTextParts.length > 1 ? messageTextParts[1] : null;

        TelegramCommandEvent event = new TelegramCommandEvent(
                message.getChat().getId(),
                message.getFrom().getId(),
                buildUserName(message),
                command,
                text,
                message.getMessageId()
        );

        publisher.publishEvent(event);
    }

    private String buildUserName(TelegramMessageDTO message) {
        String firstName = message.getFrom().getFirstName();
        String lastName = message.getFrom().getLastName();

        String fullName = (firstName == null ? "" : firstName) + " " + (lastName == null ? "" : lastName);
        String normalized = fullName.trim();

        return normalized.isEmpty() ? "Unknown" : normalized;
    }
}
