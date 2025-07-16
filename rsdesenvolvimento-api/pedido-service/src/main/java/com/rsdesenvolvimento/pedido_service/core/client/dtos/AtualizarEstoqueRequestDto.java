package com.rsdesenvolvimento.pedido_service.core.client.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtualizarEstoqueRequestDto {

    private Long produtoId;
    private Integer quantidade;
}
