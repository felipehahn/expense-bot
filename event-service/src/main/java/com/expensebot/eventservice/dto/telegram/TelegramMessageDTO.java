package com.expensebot.eventservice.dto.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TelegramMessageDTO {

    @JsonProperty("message_id")
    private Long messageId;

    private TelegramUserDTO from;

    private TelegramChatDTO chat;

    private Long date;

    private String text;

    private List<TelegramEntityDTO> entities;
}
