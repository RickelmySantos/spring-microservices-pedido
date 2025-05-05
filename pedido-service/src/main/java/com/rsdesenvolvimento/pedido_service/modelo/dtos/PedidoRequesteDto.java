package com.rsdesenvolvimento.pedido_service.modelo.dtos;

import lombok.Data;

@Data
public class PedidoRequesteDto {

  private String descricao;
  private Long usuarioId;
}
