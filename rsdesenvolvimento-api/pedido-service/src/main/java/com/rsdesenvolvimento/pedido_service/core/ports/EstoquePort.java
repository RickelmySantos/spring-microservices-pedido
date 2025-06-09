package com.rsdesenvolvimento.pedido_service.core.ports;

import com.rsdesenvolvimento.pedido_service.core.client.dtos.ReservaEstoqueRequestDto;
import java.util.List;

public interface EstoquePort {

    Boolean validarEstoque(List<ReservaEstoqueRequestDto> itens);

    void reservarEstoque(List<ReservaEstoqueRequestDto> itens);
}
