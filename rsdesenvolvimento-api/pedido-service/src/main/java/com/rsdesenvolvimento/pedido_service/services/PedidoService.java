package com.rsdesenvolvimento.pedido_service.services;

import com.rsdesenvolvimento.pedido_service.core.client.dtos.EstoqueResponseDto;
import com.rsdesenvolvimento.pedido_service.core.ports.UsuarioPort;
import com.rsdesenvolvimento.pedido_service.modelo.dtos.ItemPedidoRequestDto;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final PedidoMapper pedidoMapper;
    private final EstoqueService estoqueService;
    private final NotificacaoService notificacaoService;
    private final UsuarioPort usuarioPort;
    private final PedidoValidacaoService validacaoService;
    private final OrquestacaoEstoqueService orquestacaoEstoqueService;

    @Transactional
    public PedidoResponseDto criarPedido(PedidoRequesteDto dto) {
        PedidoService.log.info("Iniciando criação de pedido");

        Usuario usuario = this.buscarUsuarioAtual();

        this.validacaoService.validarPedido(dto);

        this.validacaoService.validarDisponibilidadeEstoque(dto.getItensPedido());

        Pedido pedido = this.criarEPersistirPedido(dto, usuario);

        this.orquestacaoEstoqueService.atualizarEstoqueParaPedido(dto.getItensPedido());

        this.enviarNotificacaoAsync(pedido);

        PedidoService.log.info("Pedido {} criado com sucesso", pedido.getId());
        return this.pedidoMapper.paraDto(pedido);
    }

    @Transactional
    public void atualizarStatusPagamento(Long pedidoId, String status) {
        PedidoService.log.info("Atualizando status do pedido {} para {}", pedidoId, status);

        Pedido pedido = this.buscarPedidoPorId(pedidoId);
        StatusEnum novoStatus = this.validacaoService.validarEConverterStatus(status);

        pedido.setStatus(novoStatus);
        this.pedidoRepository.save(pedido);

        PedidoService.log.info("Status do pedido {} atualizado para {}", pedidoId, novoStatus);
    }

    @Transactional(readOnly = true)
    public String buscarEmailPorPedidoId(Long pedidoId) {
        PedidoService.log.debug("Buscando email para pedido {}", pedidoId);
        return this.buscarPedidoPorId(pedidoId).getEmailUsuario();
    }


    private Usuario buscarUsuarioAtual() {
        Usuario usuario = this.usuarioPort.buscarUsuario();
        PedidoService.log.info("Usuário obtido: {}", usuario.getUsername());
        return usuario;
    }

    private Pedido criarEPersistirPedido(PedidoRequesteDto dto, Usuario usuario) {
        Pedido pedido = this.prepararPedido(dto, usuario);
        return this.pedidoRepository.save(pedido);
    }


    private Pedido buscarPedidoPorId(Long pedidoId) {
        return this.pedidoRepository.findById(pedidoId).orElseThrow(
                () -> new PedidoNaoEncontradoException("Pedido não encontrado: " + pedidoId));
    }

    private void enviarNotificacaoAsync(Pedido pedido) {
        try {
            this.notificacaoService.enviarNotificacao(pedido);
        } catch (Exception e) {
            PedidoService.log.error("Erro ao enviar notificação para pedido {}: {}", pedido.getId(),
                    e.getMessage());

        }
    }

    private Pedido prepararPedido(PedidoRequesteDto dto, Usuario usuario) {
        Pedido pedido = this.pedidoMapper.paraEntidade(dto);

        this.configurarDadosPedido(pedido, usuario, dto);

        if (this.temItens(dto.getItensPedido())) {
            List<ItemPedido> itens = this.criarItensPedido(dto.getItensPedido(), pedido);
            pedido.setItensPedido(itens);
        }

        return pedido;
    }

    private void configurarDadosPedido(Pedido pedido, Usuario usuario, PedidoRequesteDto dto) {
        pedido.setDataHoraCriacao(LocalDateTime.now());
        pedido.setUsuarioId(usuario.getId());
        pedido.setNomeUsuario(usuario.getUsername());
        pedido.setEmailUsuario(usuario.getEmail());
        pedido.setStatus(StatusEnum.PENDENTE);
        pedido.setObservacao(dto.getObservacao());
    }

    private List<ItemPedido> criarItensPedido(List<ItemPedidoRequestDto> itensPedido,
            Pedido pedido) {
        return itensPedido.stream().map(itemDto -> this.criarItemPedido(itemDto, pedido)).toList();
    }

    private ItemPedido criarItemPedido(ItemPedidoRequestDto itemDto, Pedido pedido) {
        EstoqueResponseDto produto = this.estoqueService.buscarProduto(this.getProdutoId(itemDto));

        ItemPedido itemPedido = this.pedidoMapper.itemPedidoRequestDtoParaItemPedido(itemDto);
        itemPedido.setNomeProduto(produto.getNome());
        itemPedido.setPedido(pedido);

        return itemPedido;
    }

    private boolean temItens(List<ItemPedidoRequestDto> itens) {
        return itens != null && !itens.isEmpty();
    }

    private Long getProdutoId(ItemPedidoRequestDto itemDto) {
        return itemDto.getProdutoId();
    }

    public static class PedidoNaoEncontradoException extends RuntimeException {
        public PedidoNaoEncontradoException(String message) {
            super(message);
        }
    }
}
