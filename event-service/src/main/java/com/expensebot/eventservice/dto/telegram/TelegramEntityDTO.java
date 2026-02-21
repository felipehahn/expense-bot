package com.expensebot.eventservice.dto.telegram;

import lombok.Data;

@Data
public class TelegramEntityDTO {
    private Integer offset;
    private Integer length;
    private String type;
}