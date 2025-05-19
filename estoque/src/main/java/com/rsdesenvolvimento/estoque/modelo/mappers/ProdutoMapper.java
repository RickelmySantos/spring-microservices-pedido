package com.rsdesenvolvimento.estoque.modelo.mappers;

import com.rsdesenvolvimento.estoque.modelo.dtos.ProdutoRequestDto;
import com.rsdesenvolvimento.estoque.modelo.dtos.ProdutoResponseDto;
import com.rsdesenvolvimento.estoque.modelo.entidade.Produto;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {

  Produto toEntity(ProdutoRequestDto dto);

  ProdutoResponseDto toDto(Produto produto);

  List<ProdutoResponseDto> toDtoList(List<Produto> produtos);

}
