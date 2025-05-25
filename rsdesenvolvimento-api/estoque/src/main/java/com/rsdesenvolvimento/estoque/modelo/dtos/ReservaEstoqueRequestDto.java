package com.rsdesenvolvimento.estoque.modelo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaEstoqueRequestDto {

  private Long produtoId;
  private Integer quantidade;
}
