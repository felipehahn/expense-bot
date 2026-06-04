package com.financebot.contracts.event;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TelegramInlineKeyboardButton(
        @JsonProperty("text") String text,
        @JsonProperty("callback_data") String callbackData
) {}
