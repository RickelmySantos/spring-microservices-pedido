package com.rsdesenvolvimento.notificacao_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

  public static final String QUEUE_NAME = "pedidoQueue";
  public static final String EXCHANGE_NAME = "pedidoExchange";
  public static final String ROUTING_KEY = "pedido.routing.key";

  @Bean
  public Queue queue() {
    return new Queue(RabbitConfig.QUEUE_NAME, true);
  }

  @Bean
  public DirectExchange exchange() {
    return new DirectExchange(RabbitConfig.EXCHANGE_NAME);
  }

  @Bean
  public Binding binding(Queue queue, DirectExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with(RabbitConfig.ROUTING_KEY);
  }



}
