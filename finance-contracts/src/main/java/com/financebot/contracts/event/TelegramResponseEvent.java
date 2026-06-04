package com.financebot.contracts.event;

public record TelegramResponseEvent(Long chatId, String text, Object replyMarkup) {}
