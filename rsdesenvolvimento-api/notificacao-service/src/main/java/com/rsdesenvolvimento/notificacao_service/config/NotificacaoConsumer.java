package com.rsdesenvolvimento.notificacao_service.config;

import com.rsdesenvolvimento.notificacao_service.service.NotificacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificacaoConsumer {

  private final NotificacaoService notificacaoService;

  @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
  public void processarNotificacao(String body) {
    try {
      System.out.println("Notificação recebida: " + body);

      String to = "rickrickelmy98@gmail.com";
      String subject;
      String content;
      String[] partes = body.split(":");

      if (partes.length > 1 && "EMAIL_PAGAMENTO_CONFIRMADO".equals(partes[0])) {
        String pedidoId = partes[1];
        subject = "Pagamento Confirmado! ✅";
        content = "<h2 style='color: green;'>Pagamento do pedido #" + pedidoId
            + " confirmado com sucesso!</h2>"
            + "<p>Obrigado por confiar em nossa plataforma. Seu pedido está sendo processado.</p>"
            + "<hr><p style='font-size: 12px; color: gray;'>Este e-mail é automático, por favor não responda.</p>";
      } else {
        subject = "Confirmação do seu pedido! 📦";
        content =
            """
                <h2 style='color: #3498db;'>Seu pedido foi confirmado e está aguardando o pagamento!</h2>\
                <p>Obrigado por escolher nossa empresa!</p>\
                <hr><p style='font-size: 12px; color: gray;'>Este e-mail é automático, por favor não responda.</p>""";
      }

      this.notificacaoService.enviarEmail(to, subject, content);
    } catch (Exception e) {
      System.err.println("Erro ao processar notificação: " + e.getMessage());
    }
  }

}
