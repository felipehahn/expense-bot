package com.expensebot.expenseservice.event.factory;

import com.expensebot.expenseservice.event.contract.IProcessEventService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProcessEventFactory {

    private final Map<String, IProcessEventService> handlers;

    public ProcessEventFactory(List<IProcessEventService> processEvents) {
        this.handlers = processEvents.stream()
                .collect(Collectors.toMap(IProcessEventService::getCommand, Function.identity()));
    }

    public IProcessEventService getHandler(String command) {
        return handlers.getOrDefault(command, handlers.get("/default"));
    }
}
