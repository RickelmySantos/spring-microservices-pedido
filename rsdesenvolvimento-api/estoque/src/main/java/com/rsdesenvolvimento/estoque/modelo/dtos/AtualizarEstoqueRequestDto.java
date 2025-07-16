package com.rsdesenvolvimento.estoque.modelo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AtualizarEstoqueRequestDto {

    private Long produtoId;
    private Integer quantidade;
}
