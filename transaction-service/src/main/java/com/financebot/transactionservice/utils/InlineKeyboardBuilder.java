package com.financebot.transactionservice.utils;

import com.financebot.contracts.event.TelegramInlineKeyboardButton;
import com.financebot.contracts.event.TelegramInlineKeyboardMarkup;

import java.util.List;

public class InlineKeyboardBuilder {
    public static TelegramInlineKeyboardMarkup build(List<List<TelegramInlineKeyboardButton>> rows) {
        return new TelegramInlineKeyboardMarkup(rows);
    }

    public static TelegramInlineKeyboardButton button(String text, String callbackData) {
        return new TelegramInlineKeyboardButton(text, callbackData);
    }
}
