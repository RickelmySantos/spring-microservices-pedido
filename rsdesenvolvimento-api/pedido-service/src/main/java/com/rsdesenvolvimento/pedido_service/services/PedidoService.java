package com.rsdesenvolvimento.pedido_service.services;

import com.rsdesenvolvimento.pedido_service.core.client.EstoqueFeignClient;
import com.rsdesenvolvimento.pedido_service.core.client.NotificacaoProducer;
import com.rsdesenvolvimento.pedido_service.core.client.UsuarioClient;
import com.rsdesenvolvimento.pedido_service.core.config.SecurityUtil;
import com.rsdesenvolvimento.pedido_service.modelo.dtos.PedidoRequesteDto;
import com.rsdesenvolvimento.pedido_service.modelo.dtos.PedidoResponseDto;
import com.rsdesenvolvimento.pedido_service.modelo.entidades.Pedido;
import com.rsdesenvolvimento.pedido_service.modelo.enums.StatusEnum;
import com.rsdesenvolvimento.pedido_service.modelo.mappers.PedidoMapper;
import com.rsdesenvolvimento.pedido_service.repositorios.PedidoRepository;
import feign.FeignException;
import java.time.LocalDateTime;
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
    private final UsuarioClient usuarioClient;
    private final EstoqueFeignClient estoqueFeignClient;
    private final NotificacaoProducer notificacaoProducer;

    public PedidoResponseDto criarPedido(String userId, PedidoRequesteDto dto) {
        try {
            // UsuarioDto usuario = this.usuarioClient.buscarUsuarioPorId(dto.getUsuarioId());

            String username = SecurityUtil.getUsername();
            String email = SecurityUtil.getUserEmail();

            boolean disponivel = this.estoqueFeignClient.validarEstoque(dto.getItens());

            if (!disponivel) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Estoque insuficiente para um ou mais produtos.");
            }

            var pedido = this.pedidoMapper.paraEntidade(dto);
            pedido.setDataHoraCriacao(LocalDateTime.now());
            pedido.setUsuarioId(userId);
            pedido.setNomeUsuario(username);
            pedido.setEmailUsuario(email);
            pedido.setStatus(StatusEnum.PENDENTE);

            Pedido pedidoSalvo = this.pedidoRepository.save(pedido);
            PedidoResponseDto response = this.pedidoMapper.paraDto(pedidoSalvo);

            response.setNomeUsuario(username);

            String mensagem = String.format("Pedido %s criado com sucesso!", pedidoSalvo.getId());
            this.notificacaoProducer.enviarNotificacao(mensagem);


            return response;
        } catch (FeignException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário não encontrado");
        }
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
}

