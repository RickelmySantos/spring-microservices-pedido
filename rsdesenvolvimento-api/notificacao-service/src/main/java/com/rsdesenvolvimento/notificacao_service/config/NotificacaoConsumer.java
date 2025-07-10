package com.rsdesenvolvimento.notificacao_service.config;


import com.rsdesenvolvimento.core.events.dto.NotificacaoDto;
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
    public void processarNotificacao(NotificacaoDto notificacao) {
        try {
            NotificacaoConsumer.log.info("Notificação recebida: {}", notificacao.getTo());

            this.notificacaoService.enviarEmail(notificacao.getTo(), notificacao.getSubject(),
                    notificacao.getContent());

            NotificacaoConsumer.log.info("E-mail enviado com sucesso para {}", notificacao.getTo());
        } catch (Exception e) {
            NotificacaoConsumer.log.error("Erro ao processar notificação para {}: {} ",
                    notificacao.getTo(), e.getMessage());
        }
    }

}
