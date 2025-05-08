package com.rsdesenvolvimento.pedido_service.core.config;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificacaoProducer {

  private final RabbitTemplate rabbitTemplate;

  public NotificacaoProducer(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  public void enviarNotificacao(String mensagem) {
    this.rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, RabbitConfig.ROUTING_KEY,
        mensagem);
  }
}
