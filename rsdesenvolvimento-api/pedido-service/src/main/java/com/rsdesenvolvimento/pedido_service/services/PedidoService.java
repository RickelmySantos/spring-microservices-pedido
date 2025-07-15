package com.rsdesenvolvimento.pedido_service.services;

import com.rsdesenvolvimento.pedido_service.core.client.dtos.ReservaEstoqueRequestDto;
import com.rsdesenvolvimento.pedido_service.core.ports.UsuarioPort;
import com.rsdesenvolvimento.pedido_service.modelo.dtos.PedidoRequesteDto;
import com.rsdesenvolvimento.pedido_service.modelo.dtos.PedidoResponseDto;
import com.rsdesenvolvimento.pedido_service.modelo.entidades.Pedido;
import com.rsdesenvolvimento.pedido_service.modelo.entidades.Usuario;
import com.rsdesenvolvimento.pedido_service.modelo.enums.StatusEnum;
import com.rsdesenvolvimento.pedido_service.modelo.mappers.PedidoMapper;
import com.rsdesenvolvimento.pedido_service.repositorios.PedidoRepository;
import java.time.LocalDateTime;
import java.util.List;
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

        List<ReservaEstoqueRequestDto> reserva = dto.getItens().stream().map(
                item -> new ReservaEstoqueRequestDto(item.getProdutoId(), item.getQuantidade()))
                .toList();

        this.estoqueService.validarEstoque(reserva);

        Pedido pedido = this.prepararPedido(dto, usuario);
        Pedido pedidoSalvo = this.pedidoRepository.save(pedido);

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


    private Pedido prepararPedido(PedidoRequesteDto dto, Usuario usuario) {
        Pedido pedido = this.pedidoMapper.paraEntidade(dto);
        pedido.setDataHoraCriacao(LocalDateTime.now());
        pedido.setUsuarioId(usuario.getId());
        pedido.setNomeUsuario(usuario.getUsername());
        pedido.setEmailUsuario(usuario.getEmail());
        pedido.setStatus(StatusEnum.PENDENTE);
        pedido.setObservacao(dto.getObservacao());

        if (pedido.getItens() != null) {
            pedido.getItens().forEach(item -> item.setPedido(pedido));
        }

        return pedido;
    }
}

