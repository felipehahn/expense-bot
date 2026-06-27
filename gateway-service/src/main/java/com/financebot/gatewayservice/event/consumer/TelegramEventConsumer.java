package com.financebot.gatewayservice.event.consumer;

import com.financebot.contracts.event.TelegramResponseEvent;
import com.financebot.contracts.messaging.RabbitMQConstants;
import com.financebot.gatewayservice.client.TelegramClient;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TelegramEventConsumer {

    @Autowired
    private TelegramClient telegramClient;

    @RabbitListener(queues = RabbitMQConstants.RESPONSE_QUEUE)
    public void consume(TelegramResponseEvent event) {
        telegramClient.sendMessage(event.chatId(), event.text(), event.replyMarkup());
    }
}
