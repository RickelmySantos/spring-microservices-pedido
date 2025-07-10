package com.rsdesenvolvimento.pedido_service.services;

import com.rsdesenvolvimento.core.events.dto.NotificacaoDto;
import com.rsdesenvolvimento.pedido_service.core.ports.NotificacaoPort;
import com.rsdesenvolvimento.pedido_service.modelo.entidades.Pedido;
import com.rsdesenvolvimento.pedido_service.modelo.enums.StatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificacaoService {

    private final NotificacaoPort notificacaoPort;

    public void enviarNotificacao(Pedido pedido) {
        String subject;
        String content;

        if (pedido.getStatus() == StatusEnum.PAGO) {
            subject = " Pagemento Confirmado! ✅";
            content = "<h2 style='color: green;'>Pagamento do pedido #" + pedido.getId()
                    + " confirmado com sucesso!</h2>" + "<p>Olá, " + pedido.getNomeUsuario()
                    + ". Obrigado por confiar em nossa plataforma. Seu pedido está sendo processado.</p>"
                    + "<hr><p style='font-size: 12px; color: gray;'>Este e-mail é automático, por favor não responda.</p>";
        } else {
            subject = "Confirmação do seu pedido! 📦";
            content = "<h2 style='color: #3498db;'>Olá, " + pedido.getNomeUsuario()
                    + ". Seu pedido #" + pedido.getId()
                    + " foi confirmado e está aguardando o pagamento!</h2>"
                    + "<p>Obrigado por escolher nossa empresa!</p>"
                    + "<hr><p style='font-size: 12px; color: gray;'>Este e-mail é automático, por favor não responda.</p>";
        }

        NotificacaoDto notificacaoDto = NotificacaoDto.builder().to(pedido.getEmailUsuario())
                .subject(subject).content(content).build();

        this.notificacaoPort.enviarNotificacao(notificacaoDto);
    }
}
