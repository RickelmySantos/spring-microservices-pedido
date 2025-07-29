package com.rsdesenvolvimento.pedido_service.services;

import com.rsdesenvolvimento.pedido_service.core.client.dtos.AtualizarEstoqueRequestDto;
import com.rsdesenvolvimento.pedido_service.core.client.dtos.EstoqueResponseDto;
import com.rsdesenvolvimento.pedido_service.core.ports.EstoquePort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class EstoqueService {

    private final EstoquePort estoquePort;

    public void validarEstoque(List<AtualizarEstoqueRequestDto> itens) {
        if (this.estoquePort.validarEstoque(itens)) {
            return;
        }
        Assert.isTrue(this.estoquePort.validarEstoque(itens),
                "Estoque insuficiente para os itens solicitados.");
    }

    public EstoqueResponseDto buscarProduto(Long id) {
        EstoqueResponseDto produto = this.estoquePort.buscarProduto(id);
        if (produto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado");
        }
        return produto;
    }

    public AtualizarEstoqueRequestDto atualizarEstoque(AtualizarEstoqueRequestDto dto) {
        EstoqueResponseDto produto = this.buscarProduto(dto.getProdutoId());
        if (produto.getEstoque() < dto.getQuantidade()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Estoque insuficiente para o produto: " + produto.getNome());
        }
        return this.estoquePort.atualizarEstoque(dto);
    }
}
