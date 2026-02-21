package com.expensebot.eventservice.controller;

import com.expensebot.eventservice.dto.telegram.TelegramUpdateDTO;
import com.expensebot.eventservice.service.TelegramWebhookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhook")
public class WebHookController {

    @Autowired
    TelegramWebhookService telegramWebhookService;

    @PostMapping("/telegram")
    public ResponseEntity<Void> receiveEvent(@RequestBody TelegramUpdateDTO update) {
        telegramWebhookService.process(update);
        return ResponseEntity.ok().build();
    }
}
