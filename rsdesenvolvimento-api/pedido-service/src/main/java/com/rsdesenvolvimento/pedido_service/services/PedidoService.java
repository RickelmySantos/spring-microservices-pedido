package com.rsdesenvolvimento.pedido_service.services;

import com.rsdesenvolvimento.pedido_service.core.client.dtos.AtualizarEstoqueRequestDto;
import com.rsdesenvolvimento.pedido_service.core.client.dtos.EstoqueResponseDto;
import com.rsdesenvolvimento.pedido_service.core.ports.UsuarioPort;
import com.rsdesenvolvimento.pedido_service.modelo.dtos.PedidoRequesteDto;
import com.rsdesenvolvimento.pedido_service.modelo.dtos.PedidoResponseDto;
import com.rsdesenvolvimento.pedido_service.modelo.entidades.ItemPedido;
import com.rsdesenvolvimento.pedido_service.modelo.entidades.Pedido;
import com.rsdesenvolvimento.pedido_service.modelo.entidades.Usuario;
import com.rsdesenvolvimento.pedido_service.modelo.enums.StatusEnum;
import com.rsdesenvolvimento.pedido_service.modelo.mappers.PedidoMapper;
import com.rsdesenvolvimento.pedido_service.repositorios.PedidoRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class PedidoService {


    private final PedidoRepository pedidoRepository;
    private final PedidoMapper pedidoMapper;
    private final EstoqueService estoqueService;
    private final NotificacaoService notificacaoService;
    private final UsuarioPort usuarioPort;

    public PedidoResponseDto criarPedido(PedidoRequesteDto dto) {

        Usuario usuario = this.usuarioPort.buscarUsuario();
        PedidoService.log.info("Usuário obtido: {}", usuario);

        if (dto.getItensPedido() == null || dto.getItensPedido().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "A lista de itens do pedido não pode ser vazia");
        }

        dto.getItensPedido().forEach(item -> {
            EstoqueResponseDto produto = this.estoqueService.buscarProduto(item.getProdutoId());
            if (produto.getEstoque() < item.getQuantidade()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Estoque insuficiente para o produto: " + produto.getNome());
            }
        });

        Pedido pedido = this.prepararPedido(dto, usuario);
        Pedido pedidoSalvo = this.pedidoRepository.save(pedido);

        if (dto.getItensPedido() != null && !dto.getItensPedido().isEmpty()) {
            dto.getItensPedido().forEach(item -> {
                AtualizarEstoqueRequestDto atualizaEstoque = new AtualizarEstoqueRequestDto();
                atualizaEstoque.setProdutoId(item.getProdutoId());
                atualizaEstoque.setQuantidade(item.getQuantidade());
                this.estoqueService.atualizarEstoque(atualizaEstoque);
            });
        }


        this.notificacaoService.enviarNotificacao(pedidoSalvo);

        return this.pedidoMapper.paraDto(pedidoSalvo);
    }

    public void statusPagamento(Long id, String status) {
        PedidoService.log.info("Atualizando status do pedido com id: {} para {}", id, status);
        Pedido pedido = this.pedidoRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));

        try {
            StatusEnum novoStatus = StatusEnum.valueOf(status.toUpperCase());
            pedido.setStatus(novoStatus);
            this.pedidoRepository.save(pedido);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status inválido");
        }
    }

    public String buscarEmailPorPedidoId(Long pedidoId) {
        Pedido pedido = this.pedidoRepository.findById(pedidoId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));
        return pedido.getEmailUsuario();
    }


    private Pedido prepararPedido(PedidoRequesteDto dto, Usuario usuario) {
        Pedido pedido = this.pedidoMapper.paraEntidade(dto);

        pedido.setDataHoraCriacao(LocalDateTime.now());
        pedido.setUsuarioId(usuario.getId());
        pedido.setNomeUsuario(usuario.getUsername());
        pedido.setEmailUsuario(usuario.getEmail());
        pedido.setStatus(StatusEnum.PENDENTE);
        pedido.setObservacao(dto.getObservacao());

        if (dto.getItensPedido() != null && !dto.getItensPedido().isEmpty()) {
            List<ItemPedido> itens = dto.getItensPedido().stream().map(itemDto -> {
                EstoqueResponseDto produto =
                        this.estoqueService.buscarProduto(itemDto.getProdutoId());

                ItemPedido itemPedido =
                        this.pedidoMapper.itemPedidoRequestDtoParaItemPedido(itemDto);
                itemPedido.setNomeProduto(produto.getNome());
                itemPedido.setPedido(pedido);

                return itemPedido;
            }).collect(Collectors.toList());

            pedido.setItensPedido(itens);
        }

        return pedido;
    }
}

