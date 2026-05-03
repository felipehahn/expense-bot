package com.expensebot.eventservice.event.publisher;

import com.expensebot.contracts.event.TelegramCommandEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TelegramEventPublisher {

    private static final String TOPIC_TELEGRAM_COMMAND = "expense-events";

    @Autowired
    KafkaTemplate<String, TelegramCommandEvent> kafkaTemplate;

    public void publishEvent(TelegramCommandEvent event) {
        kafkaTemplate.send(TOPIC_TELEGRAM_COMMAND, event);
    }

}
