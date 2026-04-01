package com.expensebot.expenseservice.event.consumer;

import com.expensebot.contracts.event.TelegramCommandEvent;
import com.expensebot.expenseservice.event.contract.EventHandler;
import com.expensebot.expenseservice.event.factory.ProcessEventFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TelegramEventConsumer {

    @Autowired
    ProcessEventFactory processEventFactory;

    private static final Logger log = LoggerFactory.getLogger(TelegramEventConsumer.class);

    @KafkaListener(topics = "expense-events")
    public void consume(TelegramCommandEvent event) {
        EventHandler service = processEventFactory.getHandler(event.command());
        service.process(event);
    }
}
