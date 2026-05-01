package com.expensebot.eventservice.client;

import org.springframework.stereotype.Service;

@Service
public class TelegramClient {

    public void sendMessage(Long chatId, String text) {
//        restClient.post()
//                .uri("/sendMessage")
//                .body(new SendMessageRequest(chatId, text))
//                .retrieve()
//                .toBodilessEntity();
    }
}
