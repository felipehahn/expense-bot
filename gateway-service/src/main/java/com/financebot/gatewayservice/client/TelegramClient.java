package com.financebot.gatewayservice.client;

import com.financebot.gatewayservice.config.TelegramConfigurationProperties;
import com.financebot.gatewayservice.dto.telegram_client.SendMessageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

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

    public void sendMessage(Long chatId, String text, Object replyMarkup) {
        restClient.post()
                .uri("/sendMessage")
                .body(new SendMessageRequest(chatId, text, replyMarkup))
                .retrieve()
                .toBodilessEntity();
    }

    public void answerCallback(String callbackQueryId) {
        restClient.post()
                .uri("/answerCallbackQuery")
                .body(Map.of("callback_query_id", callbackQueryId))
                .retrieve()
                .toBodilessEntity();
    }
}
