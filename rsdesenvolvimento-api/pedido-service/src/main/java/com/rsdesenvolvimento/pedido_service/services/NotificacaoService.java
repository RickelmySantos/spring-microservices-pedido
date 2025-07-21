package com.rsdesenvolvimento.pedido_service.services;

import com.rsdesenvolvimento.pedido_service.core.ports.NotificacaoPort;
import com.rsdesenvolvimento.pedido_service.modelo.entidades.Pedido;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificacaoService {

    private final NotificacaoPort notificacaoPort;

    public void enviarNotificacao(Pedido pedido) {
        String mensagem =
                String.format("PEDIDO_CRIADO:%s:%s", pedido.getId(), pedido.getEmailUsuario());
        this.notificacaoPort.enviarNotificacao(mensagem);
    }
}
