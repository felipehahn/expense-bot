package com.financebot.transactionservice.event.publisher;

import com.financebot.contracts.event.TelegramResponseEvent;
import com.financebot.contracts.messaging.RabbitMQConstants;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TelegramEventPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(Long chatId, String text) {
        rabbitTemplate.convertAndSend(
                RabbitMQConstants.RESPONSE_EXCHANGE,
                RabbitMQConstants.RESPONSE_ROUTING,
                new TelegramResponseEvent(chatId, text, null)
        );
    }

    public void send(Long chatId, String text, Object replyMarkup) {
        rabbitTemplate.convertAndSend(
                RabbitMQConstants.RESPONSE_EXCHANGE,
                RabbitMQConstants.RESPONSE_ROUTING,
                new TelegramResponseEvent(chatId, text, replyMarkup)
        );
    }
}
