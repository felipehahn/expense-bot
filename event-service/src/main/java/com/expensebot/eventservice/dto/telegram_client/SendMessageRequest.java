package com.expensebot.eventservice.dto.telegram_client;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SendMessageRequest(@JsonProperty("chat_id") Long chatId,
                                 @JsonProperty("text") String text) {
}
