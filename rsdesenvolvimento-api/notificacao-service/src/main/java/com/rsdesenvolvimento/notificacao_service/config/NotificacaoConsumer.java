package com.rsdesenvolvimento.notificacao_service.config;

import com.rsdesenvolvimento.notificacao_service.modelo.dto.EmailDataDto;
import com.rsdesenvolvimento.notificacao_service.service.NotificacaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificacaoConsumer {
    private final NotificacaoService notificacaoService;

    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    public void processarNotificacao(String body) {
        try {

            String[] partes = body.split(":");
            EmailDataDto emailData = this.determinarDadosEmail(partes);
            this.notificacaoService.enviarEmail(emailData.getTo(), emailData.getSubject(),
                    emailData.getContent());

        } catch (Exception e) {
            NotificacaoConsumer.log.error("Erro ao processar notificação: {} ", e.getMessage());
        }
    }

    private EmailDataDto determinarDadosEmail(String[] partes) {
        if (partes.length == 0) {
            return this.criarEmailFallback();
        }

        String tipoNotificacao = partes[0];

        return switch (tipoNotificacao) {
            case "PEDIDO_CRIADO" -> this.processarPedidoCriado(partes);
            case "EMAIL_PAGAMENTO_CONFIRMADO" -> this.processarPagamentoConfirmado(partes);
            default -> this.criarEmailFallback();
        };
    }

    private EmailDataDto processarPedidoCriado(String[] partes) {
        if (partes.length <= 2) {
            return this.criarEmailFallback();
        }

        String email = partes[2];

        return new EmailDataDto(email, "Confirmação do seu pedido! 📦",
                """
                        <h2 style='color: #3498db;'>Seu pedido foi confirmado e está aguardando o pagamento!</h2>\
                        <p>Obrigado por escolher nossa empresa!</p>\
                        <hr><p style='font-size: 12px; color: p-gray;'>Este e-mail é automático, por favor não responda.</p>""");
    }

    private EmailDataDto processarPagamentoConfirmado(String[] partes) {
        if (partes.length <= 1) {
            return this.criarEmailFallback();
        }

        String pedidoId = partes[1];
        String email = partes.length > 2 ? partes[2] : "rickrickelmy98@gmail.com";

        String content = "<h2 style='color: green;'>Pagamento do pedido #" + pedidoId
                + " confirmado com sucesso!</h2>"
                + "<p>Obrigado por confiar em nossa plataforma. Seu pedido está sendo processado.</p>"
                + "<hr><p style='font-size: 12px; color: p-gray;'>Este e-mail é automático, por favor não responda.</p>";

        return new EmailDataDto(email, "Pagamento Confirmado! ✅", content);
    }

    private EmailDataDto criarEmailFallback() {
        return new EmailDataDto("rickrickelmy98@gmail.com", "Confirmação do seu pedido! 📦",
                """
                        <h2 style='color: #3498db;'>Seu pedido foi confirmado e está aguardando o pagamento!</h2>\
                        <p>Obrigado por escolher nossa empresa!</p>\
                        <hr><p style='font-size: 12px; color: p-gray;'>Este e-mail é automático, por favor não responda.</p>""");
    }
}
