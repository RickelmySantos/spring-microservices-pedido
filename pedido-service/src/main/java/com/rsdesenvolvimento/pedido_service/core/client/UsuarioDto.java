package com.rsdesenvolvimento.pedido_service.core.client;

import lombok.Data;

@Data
public class UsuarioDto {
  private Long id;
  private String nome;
  private String cpf;
  private String email;
  private boolean ativo;
  private String dataHoraCriacao;
  private String dataHoraAtualizacao;
}
