package com.rsdesenvolvimento.pedido_service.modelo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PedidoRequesteDto {

  private String descricao;
  private Long usuarioId;

}
