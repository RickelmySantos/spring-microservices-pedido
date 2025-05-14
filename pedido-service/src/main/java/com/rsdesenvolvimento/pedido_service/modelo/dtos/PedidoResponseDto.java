package com.rsdesenvolvimento.pedido_service.modelo.dtos;

import com.rsdesenvolvimento.pedido_service.modelo.enums.StatusEnum;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PedidoResponseDto {

  private Long id;
  private String descricao;
  private Long usuarioId;
  private String nomeUsuario;
  private String emailUsuario;
  private LocalDateTime dataHoraCriacao;
  private StatusEnum status;
}
