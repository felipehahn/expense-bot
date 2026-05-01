package com.expensebot.expenseservice.session;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class UserSession {
    private String command;
    private UserSessionStep step;
    private Map<String, Object> data = new HashMap<>();

    public UserSession (String command, UserSessionStep step) {
        this.command = command;
        this.step = step;
        this.data = new HashMap<String, Object>();
    }
}
