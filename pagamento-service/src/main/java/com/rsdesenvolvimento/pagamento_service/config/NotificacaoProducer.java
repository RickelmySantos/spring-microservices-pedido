package com.rsdesenvolvimento.pagamento_service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificacaoProducer {

  private final RabbitTemplate rabbitTemplate;

  public void enviarNotificacao(String mensagem) {
    if (mensagem == null || mensagem.trim().isEmpty()) {
      throw new IllegalArgumentException("Mensagem de notificação não pode ser vazia.");
    }
    this.rabbitTemplate.convertAndSend("notificacao-exchange", "notificacao-routing-key", mensagem);
  }
}
