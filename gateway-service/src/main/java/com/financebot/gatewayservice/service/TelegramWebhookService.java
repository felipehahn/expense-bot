package com.financebot.gatewayservice.service;

import com.financebot.contracts.event.TelegramCommandEvent;
import com.financebot.gatewayservice.client.TelegramClient;
import com.financebot.gatewayservice.dto.telegram.TelegramCallbackQueryDTO;
import com.financebot.gatewayservice.dto.telegram.TelegramMessageDTO;
import com.financebot.gatewayservice.dto.telegram.TelegramUpdateDTO;
import com.financebot.gatewayservice.event.publisher.TelegramEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class TelegramWebhookService {

    @Autowired
    private TelegramEventPublisher publisher;

    @Autowired
    private TelegramClient telegramClient;

    public void process(TelegramUpdateDTO update) {
        if (update.message() != null && update.message().text() != null) {
            publishCommandEvent(update);
            return;
        }

        if (update.callbackQuery() != null)
            publishCallbackEvent(update);
    }

    private void publishCommandEvent(TelegramUpdateDTO update) {
        TelegramMessageDTO message = update.message();
        String text = message.text();

        String[] messageTextParts = text.startsWith("/") ?
                text.split(" ", 2)
                : new String[]{null, text};

        String command = messageTextParts[0];
        text = messageTextParts.length > 1 ? messageTextParts[1] : null;

        TelegramCommandEvent event = new TelegramCommandEvent(
                message.chat().id(),
                message.from().id(),
                message.from().firstName(),
                command,
                text,
                message.messageId()
        );

        publisher.publishEvent(event);
    }

    private void publishCallbackEvent(TelegramUpdateDTO update) {
        TelegramCallbackQueryDTO callback = update.callbackQuery();

        TelegramCommandEvent event = new TelegramCommandEvent(
                callback.message().chat().id(),
                callback.from().id(),
                callback.from().firstName(),
                null,
                callback.data(),
                callback.message().messageId()
        );

        publisher.publishEvent(event);
        telegramClient.answerCallback(callback.id());
    }
}
