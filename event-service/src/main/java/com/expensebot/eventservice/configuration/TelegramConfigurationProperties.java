package com.expensebot.eventservice.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "telegram")
public record TelegramConfigurationProperties(String botToken, String apiUrl) {
}
