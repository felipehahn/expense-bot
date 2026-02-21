package com.expensebot.eventservice.dto.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TelegramUserDTO {

    private Long id;

    @JsonProperty("is_bot")
    private Boolean isBot;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("language_code")
    private String languageCode;
}