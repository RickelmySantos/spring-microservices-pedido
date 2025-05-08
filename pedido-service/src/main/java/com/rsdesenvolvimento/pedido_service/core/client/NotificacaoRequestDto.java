package com.rsdesenvolvimento.pedido_service.core.client;

import lombok.Data;

@Data
public class NotificacaoRequestDto {

  private String emailDestino;
  private String mensagem;

  public NotificacaoRequestDto(String emailDestino, String mensagem) {
    this.emailDestino = emailDestino;
    this.mensagem = mensagem;
  }
}
