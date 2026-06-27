package com.financebot.gatewayservice.config;

import com.financebot.contracts.messaging.RabbitMQConstants;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public DirectExchange commandExchange() {
        return new DirectExchange(RabbitMQConstants.COMMAND_EXCHANGE);
    }

    @Bean
    public Queue commandQueue() {
        return QueueBuilder.durable(RabbitMQConstants.COMMAND_QUEUE).build();
    }

    @Bean
    public Binding commandBinding() {
        return BindingBuilder.bind(commandQueue()).to(commandExchange()).with(RabbitMQConstants.COMMAND_ROUTING);
    }

    @Bean
    public DirectExchange responseExchange() {
        return new DirectExchange(RabbitMQConstants.RESPONSE_EXCHANGE);
    }

    @Bean
    public Queue responseQueue() {
        return QueueBuilder.durable(RabbitMQConstants.RESPONSE_QUEUE).build();
    }

    @Bean
    public Binding responseBinding() {
        return BindingBuilder.bind(responseQueue()).to(responseExchange()).with(RabbitMQConstants.RESPONSE_ROUTING);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
