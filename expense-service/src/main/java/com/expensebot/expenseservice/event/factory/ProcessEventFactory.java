package com.expensebot.expenseservice.event.factory;

import com.expensebot.expenseservice.event.contract.EventHandler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProcessEventFactory {

    private final Map<String, EventHandler> handlers;

    public ProcessEventFactory(List<EventHandler> processEvents) {
        this.handlers = processEvents.stream()
                .collect(Collectors.toMap(EventHandler::getCommand, Function.identity()));
    }

    public EventHandler getHandler(String command) {
        return handlers.getOrDefault(command, handlers.get("/default"));
    }
}
