package com.rsdesenvolvimento.pedido_service.modelo.mappers;

import com.rsdesenvolvimento.pedido_service.modelo.dtos.ItemPedidoRequestDto;
import com.rsdesenvolvimento.pedido_service.modelo.dtos.ItemPedidoResponseDto;
import com.rsdesenvolvimento.pedido_service.modelo.dtos.PedidoRequesteDto;
import com.rsdesenvolvimento.pedido_service.modelo.dtos.PedidoResponseDto;
import com.rsdesenvolvimento.pedido_service.modelo.entidades.ItemPedido;
import com.rsdesenvolvimento.pedido_service.modelo.entidades.Pedido;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PedidoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataHoraCriacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "dataHoraAtualizacao", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "itensPedido", source = "itensPedido")
    Pedido paraEntidade(PedidoRequesteDto dto);

    @Mapping(target = "itensPedido", source = "itensPedido")
    PedidoResponseDto paraDto(Pedido entidade);

    ItemPedido itemPedidoRequestDtoParaItemPedido(ItemPedidoRequestDto itemPedidoRequestDto);

    ItemPedidoResponseDto itemPedidoParaItemPedidoResponseDto(ItemPedido itemPedido);

    List<ItemPedido> itensPedidoRequestDtoParaItensPedido(List<ItemPedidoRequestDto> itensDto);

    List<ItemPedidoResponseDto> itensPedidoParaItensPedidoResponseDto(List<ItemPedido> itens);

}
