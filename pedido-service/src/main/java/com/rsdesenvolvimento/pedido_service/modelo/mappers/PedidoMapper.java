package com.rsdesenvolvimento.pedido_service.modelo.mappers;

import com.rsdesenvolvimento.pedido_service.modelo.dtos.PedidoRequesteDto;
import com.rsdesenvolvimento.pedido_service.modelo.dtos.PedidoResponseDto;
import com.rsdesenvolvimento.pedido_service.modelo.entidades.Pedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PedidoMapper {

  @Mapping(target = "dataHoraCriacao", ignore = true)
  @Mapping(target = "criadoPor", ignore = true)
  @Mapping(target = "dataHoraAtualizacao", ignore = true)
  @Mapping(target = "atualizadoPor", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "nomeUsuario", ignore = true)
  @Mapping(target = "emailUsuario", ignore = true)
  @Mapping(target = "status", ignore = true)
  Pedido paraEntidade(PedidoRequesteDto dto);

  PedidoResponseDto paraDto(Pedido entidade);

}
