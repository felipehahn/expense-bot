package com.expensebot.expenseservice.event.handler;

import com.expensebot.contracts.event.TelegramCommandEvent;
import com.expensebot.expenseservice.event.contract.IProcessEventService;
import com.expensebot.expenseservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProcessEventInit implements IProcessEventService {

    @Autowired
    private UserService userService;

    @Override
    public String getCommand() {
        return "/start";
    }

    @Override
    public void process(TelegramCommandEvent event) {
        userService.createIfNotExists(event.userId(), event.userName());
    }
}
