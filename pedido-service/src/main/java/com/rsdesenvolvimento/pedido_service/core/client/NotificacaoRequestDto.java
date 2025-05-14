package com.rsdesenvolvimento.pedido_service.core.client;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NotificacaoRequestDto {

  private String emailDestino;
  private String mensagem;

  public NotificacaoRequestDto(String emailDestino, String mensagem) {
    this.emailDestino = emailDestino;
    this.mensagem = mensagem;
  }
}
