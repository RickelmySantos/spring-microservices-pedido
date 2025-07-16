package com.rsdesenvolvimento.pedido_service.core.ports;

import com.rsdesenvolvimento.pedido_service.core.client.dtos.AtualizarEstoqueRequestDto;
import com.rsdesenvolvimento.pedido_service.core.client.dtos.EstoqueResponseDto;
import java.util.List;

public interface EstoquePort {

    Boolean validarEstoque(List<AtualizarEstoqueRequestDto> itens);

    void reservarEstoque(List<AtualizarEstoqueRequestDto> itens);

    EstoqueResponseDto buscarProduto(Long id);

    AtualizarEstoqueRequestDto atualizarEstoque(AtualizarEstoqueRequestDto dto);
}
