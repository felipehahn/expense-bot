package com.expensebot.expenseservice.event.handler;

import com.expensebot.contracts.event.TelegramCommandEvent;
import com.expensebot.expenseservice.event.contract.IProcessEventService;
import org.springframework.stereotype.Service;

@Service
public class ProcessEventNotFound implements IProcessEventService {

    @Override
    public String getCommand() {
        return "/default";
    }

    @Override
    public void process(TelegramCommandEvent event) {

    }
}
