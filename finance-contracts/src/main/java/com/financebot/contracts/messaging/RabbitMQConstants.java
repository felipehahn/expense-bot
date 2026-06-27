package com.financebot.contracts.messaging;

public final class RabbitMQConstants {
    public static final String COMMAND_EXCHANGE  = "command.exchange";
    public static final String COMMAND_QUEUE     = "command.queue";
    public static final String COMMAND_ROUTING   = "command.routing";

    public static final String RESPONSE_EXCHANGE = "response.exchange";
    public static final String RESPONSE_QUEUE    = "response.queue";
    public static final String RESPONSE_ROUTING  = "response.routing";

    private RabbitMQConstants() {}
}
