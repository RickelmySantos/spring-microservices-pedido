package com.rsdesenvolvimento.pedido_service.modelo.dtos;

import java.util.List;
import com.rsdesenvolvimento.pedido_service.core.client.dtos.ReservaEstoqueRequestDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PedidoRequesteDto {

  @NotNull(message = "A descrição não pode ser nula")
  private String descricao;

  @NotNull(message = "O usuário ID não pode ser nulo")
  private Long usuarioId;

  private List<ReservaEstoqueRequestDto> itens;

}
