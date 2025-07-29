package com.rsdesenvolvimento.pedido_service.services;

import com.rsdesenvolvimento.pedido_service.core.client.dtos.EstoqueResponseDto;
import com.rsdesenvolvimento.pedido_service.modelo.dtos.ItemPedidoRequestDto;
import com.rsdesenvolvimento.pedido_service.modelo.dtos.PedidoRequesteDto;
import com.rsdesenvolvimento.pedido_service.modelo.enums.StatusEnum;
import com.rsdesenvolvimento.pedido_service.services.exceptions.EstoqueInsuficienteException;
import com.rsdesenvolvimento.pedido_service.services.exceptions.PedidoValidationException;
import com.rsdesenvolvimento.pedido_service.services.exceptions.StatusInvalidoException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PedidoValidacaoService {

  private final EstoqueService estoqueService;

  public void validarPedido(PedidoRequesteDto dto) {
    PedidoValidacaoService.log.debug("Validando dados do pedido");

    if (dto == null) {
      throw new PedidoValidationException("Dados do pedido não podem ser nulos");
    }

    if (dto.getItensPedido() == null || dto.getItensPedido().isEmpty()) {
      throw new PedidoValidationException("A lista de itens do pedido não pode ser vazia");
    }

    this.validarItens(dto.getItensPedido());
  }


  public void validarDisponibilidadeEstoque(List<ItemPedidoRequestDto> itens) {
    PedidoValidacaoService.log.debug("Validando disponibilidade no estoque para {} itens",
        itens.size());

    for (ItemPedidoRequestDto item : itens) {
      EstoqueResponseDto produto = this.estoqueService.buscarProduto(item.getProdutoId());

      if (produto.getEstoque() < item.getQuantidade()) {
        throw new EstoqueInsuficienteException(String.format(
            "Estoque insuficiente para o produto '%s'. " + "Disponível: %d, Solicitado: %d",
            produto.getNome(), produto.getEstoque(), item.getQuantidade()));
      }
    }
  }


  public StatusEnum validarEConverterStatus(String status) {
    if (status == null || status.trim().isEmpty()) {
      throw new StatusInvalidoException("Status não pode ser vazio");
    }

    try {
      return StatusEnum.valueOf(status.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new StatusInvalidoException("Status inválido: " + status);
    }
  }

  private void validarItens(List<ItemPedidoRequestDto> itens) {
    for (ItemPedidoRequestDto item : itens) {
      if (item.getProdutoId() == null) {
        throw new PedidoValidationException("ID do produto é obrigatório");
      }

      if (item.getQuantidade() == null || item.getQuantidade() <= 0) {
        throw new PedidoValidationException("Quantidade deve ser maior que zero");
      }

      if (item.getPrecoUnitario() == null
          || item.getPrecoUnitario().compareTo(java.math.BigDecimal.ZERO) <= 0) {
        throw new PedidoValidationException("Preço deve ser maior que zero");
      }
    }
  }
}
