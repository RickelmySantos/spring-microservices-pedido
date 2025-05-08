package com.rsdesenvolvimento.notificacao_service.config;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NotificacaoConsumer {


  @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
  public void processarNotificacao(String mensagem) {

    System.out.println(String.format("Notificação recebida: %s", mensagem));
  }
}
