package com.rsdesenvolvimento.pedido_service.core.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "usuario-api")
public interface UsuarioClient {

  @GetMapping("/api/usuarios/{id}")
  UsuarioDto buscarUsuarioPorId(@PathVariable Long id);
}
