package com.expensebot.eventservice.dto.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TelegramCommandEvent {
    private Long chatId;
    private Long userId;
    private String command;
    private String text;
    private Long messageId;
}