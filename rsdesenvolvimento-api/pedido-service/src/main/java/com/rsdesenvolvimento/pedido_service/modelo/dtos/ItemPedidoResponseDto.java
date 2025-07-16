package com.rsdesenvolvimento.pedido_service.modelo.dtos;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemPedidoResponseDto {

    private Long id;

    private Long produtoId;

    private String nomeProduto;

    private Integer quantidade;

    private BigDecimal precoUnitario;


}
