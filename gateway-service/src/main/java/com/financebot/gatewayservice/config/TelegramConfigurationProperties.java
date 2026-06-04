package com.financebot.gatewayservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "telegram")
public record TelegramConfigurationProperties(String botToken, String apiUrl) {
}
