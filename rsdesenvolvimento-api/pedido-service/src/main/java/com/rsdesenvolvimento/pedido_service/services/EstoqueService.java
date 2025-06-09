package com.rsdesenvolvimento.pedido_service.services;

import com.rsdesenvolvimento.pedido_service.core.client.dtos.ReservaEstoqueRequestDto;
import com.rsdesenvolvimento.pedido_service.core.ports.EstoquePort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class EstoqueService {

    private final EstoquePort estoquePort;

    public void validarEstoque(List<ReservaEstoqueRequestDto> itens) {
        if (!this.estoquePort.validarEstoque(itens)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Estoque insuficiente para os itens solicitados.");
        }
    }
}
