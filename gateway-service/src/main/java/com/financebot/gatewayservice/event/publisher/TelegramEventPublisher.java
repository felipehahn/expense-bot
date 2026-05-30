package com.financebot.gatewayservice.event.publisher;

import com.financebot.contracts.event.TelegramCommandEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TelegramEventPublisher {

    private static final String TOPIC_TELEGRAM_COMMAND = "command-events";

    @Autowired
    KafkaTemplate<String, TelegramCommandEvent> kafkaTemplate;

    public void publishEvent(TelegramCommandEvent event) {
        kafkaTemplate.send(TOPIC_TELEGRAM_COMMAND, event);
    }

}
