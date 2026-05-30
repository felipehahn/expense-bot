package com.financebot.gatewayservice.service;

import com.financebot.contracts.event.TelegramCommandEvent;
import com.financebot.gatewayservice.dto.telegram.TelegramMessageDTO;
import com.financebot.gatewayservice.dto.telegram.TelegramUpdateDTO;
import com.financebot.gatewayservice.event.publisher.TelegramEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
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
                command,
                text,
                message.getMessageId()
        );

        publisher.publishEvent(event);
    }
}
