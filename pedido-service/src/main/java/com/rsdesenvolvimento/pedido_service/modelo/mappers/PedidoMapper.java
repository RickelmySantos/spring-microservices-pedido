package com.rsdesenvolvimento.pedido_service.modelo.mappers;

import com.rsdesenvolvimento.pedido_service.modelo.dtos.PedidoRequesteDto;
import com.rsdesenvolvimento.pedido_service.modelo.dtos.PedidoResponseDto;
import com.rsdesenvolvimento.pedido_service.modelo.entidades.Pedido;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PedidoMapper {

  Pedido paraEntidade(PedidoRequesteDto dto);

  PedidoResponseDto paraDto(Pedido entidade);

}
