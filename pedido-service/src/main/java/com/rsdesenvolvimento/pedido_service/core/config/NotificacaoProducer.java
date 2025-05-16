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

  // public void enviarNotificacao(String jsonPedido) {
  // try {
  // // ✅ Enviar diretamente, sem nova conversão
  // this.rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, RabbitConfig.ROUTING_KEY,
  // jsonPedido);
  // System.out.println("✅ Notificação enviada ao RabbitMQ: " + jsonPedido);
  // } catch (Exception e) {
  // System.err.println("🚨 Erro ao enviar notificação: " + e.getMessage());
  // }
  // }

}
