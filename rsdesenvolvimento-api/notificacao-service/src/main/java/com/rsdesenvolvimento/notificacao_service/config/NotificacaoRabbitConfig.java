package com.rsdesenvolvimento.notificacao_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificacaoRabbitConfig {
    @Bean
    public DirectExchange notificacaoExchange() {
        return new DirectExchange("notificacao-exchange");
    }

    @Bean
    public DirectExchange notificacaoDelayExchange() {
        return new DirectExchange("notificacao-delay-exchange");
    }

    @Bean
    public Queue notificacaoQueue() {
        return QueueBuilder.durable("notificacao-queue").build();
    }

    @Bean
    public Queue notificacaoDelayQueue() {
        return QueueBuilder.durable("notificacao-delay-queue")
                .withArgument("x-dead-letter-exchange", "notificacao-exchange")
                .withArgument("x-dead-letter-routing-key", "notificacao-routing-key").build();
    }

    @Bean
    public Binding notificacaoBinding(Queue notificacaoQueue, DirectExchange notificacaoExchange) {
        return BindingBuilder.bind(notificacaoQueue).to(notificacaoExchange)
                .with("notificacao-routing-key");
    }

    @Bean
    public Binding notificacaoDelayBinding(Queue notificacaoDelayQueue,
            DirectExchange notificacaoDelayExchange) {
        return BindingBuilder.bind(notificacaoDelayQueue).to(notificacaoDelayExchange)
                .with("notificacao.delay");
    }
}
