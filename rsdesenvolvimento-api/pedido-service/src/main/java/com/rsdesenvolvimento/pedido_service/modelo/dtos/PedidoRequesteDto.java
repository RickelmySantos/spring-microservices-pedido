package com.rsdesenvolvimento.pedido_service.modelo.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PedidoRequesteDto {


    private String usuarioId;

    private String observacao;

    private String nomeUsuario;

    private String emailUsuario;

    @NotNull(message = "A lista de itens do pedido é obrigatória")
    @NotEmpty(message = "A lista de itens do pedido não pode ser vazia")
    @Valid
    private List<ItemPedidoRequestDto> itensPedido;


}
