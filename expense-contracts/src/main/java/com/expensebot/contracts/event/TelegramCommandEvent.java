package com.expensebot.contracts.event;

public record TelegramCommandEvent(
        Long chatId,
        Long userId,
        String userName,
        String command,
        String text,
        Long messageId
) {
}
