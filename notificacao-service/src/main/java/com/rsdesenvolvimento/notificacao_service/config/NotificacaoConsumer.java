package com.rsdesenvolvimento.notificacao_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rsdesenvolvimento.notificacao_service.service.NotificacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificacaoConsumer {

  private final NotificacaoService notificacaoService;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
  public void processarNotificacao(String body) {

    try {

      // PedidoDto pedido = this.objectMapper.readValue(body, PedidoDto.class);

      System.out.println(String.format("Notificação recebida: %s", body));

      String to = "silvahelois311@gmail.com";
      String subject = "Confirmação do seu pedido! 📦";
      String content =
          """
              <h2 style='color: #3498db;'>Seu pedido foi confirmado!</h2>\
              <p>Obrigado por escolher nossa empresa!</p>\
              <hr><p style='font-size: 12px; color: gray;'>Este e-mail é automático, por favor não responda.</p>""";
      this.notificacaoService.enviarEmail(to, subject, content);
    } catch (Exception e) {
      System.out.println("Erro ao processar notificação: " + e.getMessage());
    }
  }
}
