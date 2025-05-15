package com.rsdesenvolvimento.pedido_service.core.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "usuario-api", path = "/api/usuarios",
    fallbackFactory = UsuarioClientFallback.class)
public interface UsuarioClient {

  @CircuitBreaker(name = "usuario-api", fallbackMethod = "fallbackBuscarUsuarioPorId")
  @Retry(name = "usuario-api")
  @GetMapping("/{id}")
  UsuarioDto buscarUsuarioPorId(@PathVariable Long id);

  default UsuarioDto fallbackBuscarUsuarioPorId(Long id, Throwable throwable) {
    UsuarioDto fallback = new UsuarioDto();
    fallback.setId(id);
    fallback.setNome("Usuário não encontrado - fallback ativado");
    return fallback;
  }
}
