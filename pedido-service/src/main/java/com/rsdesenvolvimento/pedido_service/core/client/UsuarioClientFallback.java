package com.rsdesenvolvimento.pedido_service.core.client;

import org.springframework.stereotype.Component;

@Component
public class UsuarioClientFallback implements UsuarioClient {

  @Override
  public UsuarioDto buscarUsuarioPorId(Long id) {
    UsuarioDto fallback = new UsuarioDto();
    fallback.setId(id);
    fallback.setNome("Usuário não encontrado");
    return fallback;
  }



}
