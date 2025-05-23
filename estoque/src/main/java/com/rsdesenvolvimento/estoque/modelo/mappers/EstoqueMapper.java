package com.rsdesenvolvimento.estoque.modelo.mappers;

import com.rsdesenvolvimento.estoque.modelo.dtos.EstoqueRequestDto;
import com.rsdesenvolvimento.estoque.modelo.dtos.EstoqueResponseDto;
import com.rsdesenvolvimento.estoque.modelo.entidade.Estoque;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EstoqueMapper {

  Estoque toEntity(EstoqueRequestDto dto);

  EstoqueResponseDto toDto(Estoque produto);

  List<EstoqueResponseDto> toDtoList(List<Estoque> produtos);

}
