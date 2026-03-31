package com.expensebot.expenseservice.event.handler;

import com.expensebot.contracts.event.TelegramCommandEvent;
import com.expensebot.expenseservice.event.contract.IProcessEventService;
import org.springframework.stereotype.Service;

@Service
public class ProcessEventExpenseService implements IProcessEventService {

    @Override
    public String getCommand() {
        return "/despesa";
    }

    @Override
    public void process(TelegramCommandEvent event) {

    }
}
