package com.expensebot.contracts.event;

public record TelegramResponseEvent(Long chatId, String text) {}
