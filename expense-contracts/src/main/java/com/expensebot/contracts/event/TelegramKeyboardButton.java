package com.expensebot.contracts.event;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TelegramKeyboardButton(
        @JsonProperty("text") String text
) {}
