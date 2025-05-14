package com.rsdesenvolvimento.usuario.modelo.mappers;

import com.rsdesenvolvimento.usuario.modelo.dtos.UsuarioRequestDto;
import com.rsdesenvolvimento.usuario.modelo.dtos.UsuarioResponseDto;
import com.rsdesenvolvimento.usuario.modelo.entidades.Usuario;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

  @Mapping(target = "dataHoraCriacao", ignore = true)
  @Mapping(target = "criadoPor", ignore = true)
  @Mapping(target = "dataHoraAtualizacao", ignore = true)
  @Mapping(target = "atualizadoPor", ignore = true)
  @Mapping(target = "id", ignore = true)
  Usuario toEntity(UsuarioRequestDto dto);


  UsuarioResponseDto toDto(Usuario entity);

  List<UsuarioResponseDto> toDtoList(List<Usuario> usuarios);

}
