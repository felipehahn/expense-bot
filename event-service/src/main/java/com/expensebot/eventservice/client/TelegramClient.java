package com.expensebot.eventservice.client;

import com.expensebot.contracts.event.TelegramReplyKeyboardMarkup;
import com.expensebot.eventservice.configuration.TelegramConfigurationProperties;
import com.expensebot.eventservice.dto.telegram_client.SendMessageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class TelegramClient {

    private final RestClient restClient;
    private final TelegramConfigurationProperties properties;

    public TelegramClient(RestClient.Builder builder, TelegramConfigurationProperties properties) {
        this.properties = properties;
        this.restClient = builder
                .baseUrl(properties.apiUrl() + properties.botToken())
                .build();
    }

    public void sendMessage(Long chatId, String text, TelegramReplyKeyboardMarkup replyMarkup) {
        restClient.post()
                .uri("/sendMessage")
                .body(new SendMessageRequest(chatId, text, replyMarkup))
                .retrieve()
                .toBodilessEntity();
    }
}
