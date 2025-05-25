package com.rsdesenvolvimento.pedido_service.core.client;

import com.rsdesenvolvimento.pedido_service.core.client.dtos.UsuarioDto;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class UsuarioClientFallback implements FallbackFactory<UsuarioClient> {


  @Override
  public UsuarioClient create(Throwable cause) {
    return id -> {
      System.out.println(
          "⚠️ Fallback acionado: " + cause.getClass().getSimpleName() + " - " + cause.getMessage());
      System.out.println("Fallback acionado com erro: " + cause.getMessage());
      UsuarioDto fallback = new UsuarioDto();
      fallback.setId(id);
      fallback.setNome("Usuário não encontrado (via fallback factory)");
      return fallback;
    };

  }
}
