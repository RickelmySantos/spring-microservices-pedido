package com.rsdesenvolvimento.pedido_service.modelo.dtos;

import com.rsdesenvolvimento.pedido_service.core.client.dtos.ReservaEstoqueRequestDto;
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

    @NotNull(message = "A descrição não pode ser nula")
    private String descricao;

    @NotNull(message = "O usuário ID não pode ser nulo")
    private String usuarioId;

    private List<ReservaEstoqueRequestDto> itens;

}
