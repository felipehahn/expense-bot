package com.financebot.gatewayservice.controller;

import com.financebot.gatewayservice.dto.telegram.TelegramUpdateDTO;
import com.financebot.gatewayservice.security.TelegramSecurityValidator;
import com.financebot.gatewayservice.service.TelegramWebhookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
public class WebHookController {

    @Autowired
    TelegramWebhookService telegramWebhookService;

    @Autowired
    TelegramSecurityValidator securityValidator;

    @PostMapping("/telegram")
    public ResponseEntity<Void> receiveEvent(@RequestBody TelegramUpdateDTO update,
                                             @RequestHeader(value = "X-Telegram-Bot-Api-Secret-Token", required = false) String token) {
        if (!securityValidator.isValid(token))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        telegramWebhookService.process(update);
        return ResponseEntity.ok().build();
    }
}
