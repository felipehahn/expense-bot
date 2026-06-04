package com.financebot.contracts.event;

public record TelegramCommandEvent(
        Long chatId,
        Long userId,
        String username,
        String command,
        String text,
        Long messageId
) {
}
