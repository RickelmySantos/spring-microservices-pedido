package com.rsdesenvolvimento.pedido_service.services;

import com.rsdesenvolvimento.pedido_service.core.client.NotificacaoProducer;
import com.rsdesenvolvimento.pedido_service.core.client.UsuarioClient;
import com.rsdesenvolvimento.pedido_service.core.client.UsuarioDto;
import com.rsdesenvolvimento.pedido_service.modelo.dtos.PedidoRequesteDto;
import com.rsdesenvolvimento.pedido_service.modelo.dtos.PedidoResponseDto;
import com.rsdesenvolvimento.pedido_service.modelo.entidades.Pedido;
import com.rsdesenvolvimento.pedido_service.modelo.enums.StatusEnum;
import com.rsdesenvolvimento.pedido_service.modelo.mappers.PedidoMapper;
import com.rsdesenvolvimento.pedido_service.repositorios.PedidoRepository;
import feign.FeignException.FeignClientException;
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
  private final NotificacaoProducer notificacaoProducer;

  public PedidoResponseDto criarPedido(PedidoRequesteDto dto) {
    try {
      UsuarioDto usuario = this.usuarioClient.buscarUsuarioPorId(dto.getUsuarioId());


      var pedido = this.pedidoMapper.paraEntidade(dto);
      pedido.setDataHoraCriacao(LocalDateTime.now());
      pedido.setNomeUsuario(usuario.getNome());
      pedido.setEmailUsuario(usuario.getEmail());
      pedido.setStatus(StatusEnum.PENDENTE);

      Pedido pedidoSalvo = this.pedidoRepository.save(pedido);
      PedidoResponseDto response = this.pedidoMapper.paraDto(pedidoSalvo);

      response.setNomeUsuario(usuario.getNome());

      // ObjectMapper objectMapper = new ObjectMapper();
      // String jsonPedido = objectMapper.writeValueAsString(response);

      String mensagem = String.format("Pedido %s criado com sucesso!", pedidoSalvo.getId());
      this.notificacaoProducer.enviarNotificacao(mensagem);


      return response;
    } catch (FeignClientException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário não encontrado");
    }
  }


  public void statusPagamento(Long id, String status) {
    PedidoService.log.info("Atualizando status do pedido com id: {} para {}", id, status);
    Pedido pedido = this.pedidoRepository.findById(id).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));

    if (status.equalsIgnoreCase(StatusEnum.FINALIZADO.name())) {
      pedido.setStatus(StatusEnum.FINALIZADO);
      this.pedidoRepository.save(pedido);
    } else if (status.equalsIgnoreCase(StatusEnum.CANCELADO.name())) {
      pedido.setStatus(StatusEnum.CANCELADO);
      this.pedidoRepository.save(pedido);
    } else {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status inválido");
    }
  }
}

