package com.expensebot.expenseservice.event.consumer;

import com.expensebot.contracts.event.TelegramCommandEvent;
import com.expensebot.expenseservice.event.contract.EventHandler;
import com.expensebot.expenseservice.event.factory.ProcessEventFactory;
import com.expensebot.expenseservice.session.UserSession;
import com.expensebot.expenseservice.session.UserSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TelegramEventConsumer {

    @Autowired
    ProcessEventFactory processEventFactory;

    @Autowired
    UserSessionRepository userSessionRepository;

    @KafkaListener(topics = "expense-events")
    public void consume(TelegramCommandEvent event) {
        String command = "";
        UserSession session = userSessionRepository.get(event.userId());

        if (session != null) command = session.getCommand();
        else command = event.command();

        EventHandler service = processEventFactory.getHandler(command);
        service.process(event, session);
    }
}
