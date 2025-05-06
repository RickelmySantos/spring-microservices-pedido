package com.rsdesenvolvimento.pedido_service.core.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "usuario-api", path = "/api/usuarios")
public interface UsuarioClient {

  @GetMapping("/{id}")
  UsuarioDto buscarUsuarioPorId(@PathVariable Long id);
}
