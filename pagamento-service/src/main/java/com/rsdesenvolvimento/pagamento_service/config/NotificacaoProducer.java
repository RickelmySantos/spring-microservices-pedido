package com.rsdesenvolvimento.pagamento_service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
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

  public void enviarNotificacaoComDelay(String mensagem, long delayMillis) {
    if (mensagem == null || mensagem.trim().isEmpty()) {
      throw new IllegalArgumentException("Mensagem de notificação não pode ser vazia.");
    }

    MessageProperties props = new MessageProperties();
    props.setExpiration(String.valueOf(delayMillis));
    Message message = new Message(mensagem.getBytes(), props);

    this.rabbitTemplate.convertAndSend("notificacao-delay-exchange", "notificacao.delay", message);
  }
}
