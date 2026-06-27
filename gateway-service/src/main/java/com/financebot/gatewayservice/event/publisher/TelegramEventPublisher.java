package com.financebot.gatewayservice.event.publisher;

import com.financebot.contracts.event.TelegramCommandEvent;
import com.financebot.contracts.messaging.RabbitMQConstants;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TelegramEventPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publishEvent(TelegramCommandEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConstants.COMMAND_EXCHANGE,
                RabbitMQConstants.COMMAND_ROUTING,
                event
        );
    }
}
