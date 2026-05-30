package com.financebot.transactionservice.event.consumer;

import com.financebot.contracts.event.TelegramCommandEvent;
import com.financebot.transactionservice.event.contract.EventHandler;
import com.financebot.transactionservice.event.factory.ProcessEventFactory;
import com.financebot.transactionservice.event.publisher.TelegramEventPublisher;
import com.financebot.transactionservice.exception.BotException;
import com.financebot.transactionservice.session.UserSession;
import com.financebot.transactionservice.session.UserSessionRepository;
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

    @KafkaListener(topics = "command-events")
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
