package com.expensebot.eventservice.dto.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TelegramUpdateDTO {

    @JsonProperty("update_id")
    private Long updateId;

    private TelegramMessageDTO message;
}
