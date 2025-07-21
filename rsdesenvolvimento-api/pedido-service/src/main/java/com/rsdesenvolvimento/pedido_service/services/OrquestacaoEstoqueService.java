package com.rsdesenvolvimento.pedido_service.services;

import com.rsdesenvolvimento.pedido_service.core.client.dtos.AtualizarEstoqueRequestDto;
import com.rsdesenvolvimento.pedido_service.modelo.dtos.ItemPedidoRequestDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrquestacaoEstoqueService {

    private final EstoqueService estoqueService;

    @Async
    public void atualizarEstoqueParaPedido(List<ItemPedidoRequestDto> itens) {
        OrquestacaoEstoqueService.log.info("Iniciando atualização de estoque para {} itens",
                itens.size());

        try {
            for (ItemPedidoRequestDto item : itens) {
                this.atualizarEstoqueItem(item);
            }
            OrquestacaoEstoqueService.log
                    .info("Estoque atualizado com sucesso para todos os itens");

        } catch (Exception e) {
            OrquestacaoEstoqueService.log.error("Erro ao atualizar estoque: {}", e.getMessage(), e);
            throw new RuntimeException("Falha na atualização do estoque", e);
        }
    }

    private void atualizarEstoqueItem(ItemPedidoRequestDto item) {
        OrquestacaoEstoqueService.log.debug("Atualizando estoque para produto {} - quantidade: {}",
                item.getProdutoId(), item.getQuantidade());

        AtualizarEstoqueRequestDto atualizaEstoque = new AtualizarEstoqueRequestDto();
        atualizaEstoque.setProdutoId(item.getProdutoId());
        atualizaEstoque.setQuantidade(item.getQuantidade());

        this.estoqueService.atualizarEstoque(atualizaEstoque);
    }

    public void reservarEstoqueParaPedido(List<ItemPedidoRequestDto> itens) {
        OrquestacaoEstoqueService.log.info("Reservando estoque para {} itens", itens.size());

        for (ItemPedidoRequestDto item : itens) {
            OrquestacaoEstoqueService.log.debug("Reservando produto {} - quantidade: {}",
                    item.getProdutoId(), item.getQuantidade());
        }
    }
}
