package com.financebot.contracts.event;

public record TelegramResponseEvent(Long chatId, String text, TelegramReplyKeyboardMarkup replyMarkup ) {
    public TelegramResponseEvent(Long chatId, String text) {
        this(chatId, text, null);
    }
}
