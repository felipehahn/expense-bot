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
    private Map<String, String> data = new HashMap<>();
}
