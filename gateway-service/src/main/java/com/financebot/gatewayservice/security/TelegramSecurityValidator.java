package com.financebot.gatewayservice.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Component
public class TelegramSecurityValidator {

    @Value("${telegram.secret-token}")
    private String secretToken;

    public boolean isValid(String token) {
        return MessageDigest.isEqual(
                secretToken.getBytes(StandardCharsets.UTF_8),
                token.getBytes(StandardCharsets.UTF_8)
        );
    }
}