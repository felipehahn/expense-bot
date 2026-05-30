package com.financebot.gatewayservice.client;

import com.financebot.contracts.event.TelegramReplyKeyboardMarkup;
import com.financebot.gatewayservice.configuration.TelegramConfigurationProperties;
import com.financebot.gatewayservice.dto.telegram_client.SendMessageRequest;
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
