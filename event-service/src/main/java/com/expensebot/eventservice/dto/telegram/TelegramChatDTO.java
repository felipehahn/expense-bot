package com.expensebot.eventservice.dto.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TelegramChatDTO {

    private Long id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    private String type;
}