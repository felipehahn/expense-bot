package com.expensebot.expenseservice.event.consumer;

import com.expensebot.contracts.event.TelegramCommandEvent;
import com.expensebot.expenseservice.event.contract.EventHandler;
import com.expensebot.expenseservice.event.factory.ProcessEventFactory;
import com.expensebot.expenseservice.event.publisher.TelegramEventPublisher;
import com.expensebot.expenseservice.exception.BotException;
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

    @Autowired
    TelegramEventPublisher publisher;

    @KafkaListener(topics = "expense-events")
    public void consume(TelegramCommandEvent event) {
        try {
            UserSession session = userSessionRepository.get(event.userId());
            String command = session != null ? session.getCommand() : event.command();

            EventHandler handler = processEventFactory.getHandler(command);
            handler.process(event, session);
        } catch (BotException e) {
            publisher.send(event.chatId(), "❌ " + e.getMessage());
        } catch (Exception e) {
            publisher.send(event.chatId(), "❌ Ocorreu um erro inesperado. Tente novamente.");
        }
    }
}
