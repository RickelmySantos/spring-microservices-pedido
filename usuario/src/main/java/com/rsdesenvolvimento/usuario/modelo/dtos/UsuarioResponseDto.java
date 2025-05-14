package com.rsdesenvolvimento.usuario.modelo.dtos;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponseDto {

  private Long id;
  private String nome;
  private String cpf;
  private String email;
  private boolean ativo;
  private LocalDateTime dataHoraCriacao;
  private LocalDateTime dataHoraAtualizacao;
}
