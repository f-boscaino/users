package com.intesigroup.users.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;


@Configuration
public class RabbitConfig {

    public static final String USERS_EXCHANGE = "users.exchange";
    public static final String USERS_QUEUE = "users.queue";
    public static final String USERS_ROUTING_KEY = "users.created";

    @Bean
    public DirectExchange usersExchange() {
        return new DirectExchange(USERS_EXCHANGE, true, false);
    }

    @Bean
    public Queue usersQueue() {
        return new Queue(USERS_QUEUE, true);
    }

    @Bean
    public Binding usersBinding(Queue usersQueue, DirectExchange usersExchange) {
        return BindingBuilder
                .bind(usersQueue)
                .to(usersExchange)
                .with(USERS_ROUTING_KEY);
    }
}
