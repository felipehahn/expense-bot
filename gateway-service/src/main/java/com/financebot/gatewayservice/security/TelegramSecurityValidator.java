package com.financebot.gatewayservice.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TelegramSecurityValidator {

    @Value("${telegram.secret-token}")
    private String secretToken;

    public boolean isValid(String token) {
        return secretToken.equals(token);
    }
}