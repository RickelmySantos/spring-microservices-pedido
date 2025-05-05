package com.rsdesenvolvimento.pedido_service.modelo.dtos;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class PedidoResponseDto {

  private Long id;
  private String descricao;
  private Long usuarioId;
  private LocalDateTime dataCriacao;
}
