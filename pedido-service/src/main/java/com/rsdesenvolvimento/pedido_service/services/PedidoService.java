package com.rsdesenvolvimento.pedido_service.services;

import com.rsdesenvolvimento.pedido_service.core.client.UsuarioClient;
import com.rsdesenvolvimento.pedido_service.modelo.dtos.PedidoRequesteDto;
import com.rsdesenvolvimento.pedido_service.modelo.dtos.PedidoResponseDto;
import com.rsdesenvolvimento.pedido_service.modelo.mappers.PedidoMapper;
import com.rsdesenvolvimento.pedido_service.repositorios.PedidoRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PedidoService {


  private final PedidoRepository pedidoRepository;
  private final PedidoMapper pedidoMapper;
  private final UsuarioClient usuarioClient;


  public PedidoResponseDto criarPedido(PedidoRequesteDto dto) {
    try {
      this.usuarioClient.buscarUsuarioPorId(dto.getUsuarioId());
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário não encontrado");
    }

    var pedido = this.pedidoMapper.paraEntidade(dto);
    pedido.setDataHoraCriacao(LocalDateTime.now());

    return this.pedidoMapper.paraDto(this.pedidoRepository.save(pedido));

  }

}
