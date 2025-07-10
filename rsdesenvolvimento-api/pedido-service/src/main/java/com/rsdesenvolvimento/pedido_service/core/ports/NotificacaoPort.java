package com.rsdesenvolvimento.pedido_service.core.ports;

import com.rsdesenvolvimento.core.events.dto.NotificacaoDto;

public interface NotificacaoPort {

    void enviarNotificacao(NotificacaoDto mensagem);
}
