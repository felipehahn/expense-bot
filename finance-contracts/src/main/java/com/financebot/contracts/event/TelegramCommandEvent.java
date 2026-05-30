package com.financebot.contracts.event;

public record TelegramCommandEvent(
        Long chatId,
        Long userId,
        String command,
        String text,
        Long messageId
) {
}
