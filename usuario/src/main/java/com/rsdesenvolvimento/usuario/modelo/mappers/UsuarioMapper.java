package com.rsdesenvolvimento.usuario.modelo.mappers;

import com.rsdesenvolvimento.usuario.modelo.dtos.UsuarioRequestDto;
import com.rsdesenvolvimento.usuario.modelo.dtos.UsuarioResponseDto;
import com.rsdesenvolvimento.usuario.modelo.entidades.Usuario;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

  Usuario toEntity(UsuarioRequestDto dto);

  UsuarioResponseDto toDto(Usuario entity);

  List<UsuarioResponseDto> toDtoList(List<Usuario> usuarios);

}
