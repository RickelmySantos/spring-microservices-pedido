package com.rsdesenvolvimento.pedido_service.core.client.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDto {
  private Long id;
  private String nome;
  private String cpf;
  private String email;
  private boolean ativo;
  private String dataHoraCriacao;
  private String dataHoraAtualizacao;
}
