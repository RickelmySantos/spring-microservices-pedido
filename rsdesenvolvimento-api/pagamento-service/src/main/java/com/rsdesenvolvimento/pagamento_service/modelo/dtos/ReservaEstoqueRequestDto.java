package com.rsdesenvolvimento.pagamento_service.modelo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservaEstoqueRequestDto {
  private Long produtoId;
  private Integer quantidade;


}
