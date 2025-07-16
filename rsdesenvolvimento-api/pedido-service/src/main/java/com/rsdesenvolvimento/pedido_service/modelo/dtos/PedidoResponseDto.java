package com.rsdesenvolvimento.pedido_service.modelo.dtos;

import com.rsdesenvolvimento.pedido_service.modelo.enums.StatusEnum;
import java.time.LocalDateTime;
import java.util.List;
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

    private String usuarioId;

    private String nomeUsuario;

    private String emailUsuario;

    private String observacao;

    private StatusEnum status;

    private LocalDateTime dataHoraCriacao;

    private List<ItemPedidoResponseDto> itensPedido;

}
