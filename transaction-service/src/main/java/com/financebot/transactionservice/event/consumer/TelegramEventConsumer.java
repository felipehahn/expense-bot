package com.financebot.transactionservice.event.consumer;

import com.financebot.contracts.event.TelegramCommandEvent;
import com.financebot.contracts.messaging.RabbitMQConstants;
import com.financebot.transactionservice.event.contract.EventHandler;
import com.financebot.transactionservice.event.factory.ProcessEventFactory;
import com.financebot.transactionservice.event.publisher.TelegramEventPublisher;
import com.financebot.transactionservice.exception.BotException;
import com.financebot.transactionservice.session.UserSession;
import com.financebot.transactionservice.session.UserSessionRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class TelegramEventConsumer {

    @Autowired
    private ProcessEventFactory processEventFactory;

    @Autowired
    private UserSessionRepository userSessionRepository;

    @Autowired
    private TelegramEventPublisher publisher;

    private static final Set<String> PRIORITY_COMMANDS = Set.of("/cancelar");

    @RabbitListener(queues = RabbitMQConstants.COMMAND_QUEUE)
    public void consume(TelegramCommandEvent event) {
        try {
            UserSession session = null;
            String command;

            if (event.command() != null && PRIORITY_COMMANDS.contains(event.command())) {
                command = event.command();
            } else {
                session = userSessionRepository.get(event.userId());
                command = session != null ? session.getCommand() : event.command();
            }

            EventHandler handler = processEventFactory.getHandler(command);
            handler.process(event, session);
        } catch (BotException e) {
            publisher.send(event.chatId(), "❌ " + e.getMessage());
        } catch (Exception e) {
            publisher.send(event.chatId(), "❌ Ocorreu um erro inesperado. Tente novamente.");
        }
    }
}
