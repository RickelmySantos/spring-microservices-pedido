package com.rsdesenvolvimento.pagamento_service.config;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "pedido-service", path = "/api/pedido")

public interface PedidoClient {

  static final Logger logger = LoggerFactory.getLogger(PedidoClient.class);

  @CircuitBreaker(name = "pedido-service", fallbackMethod = "fallbackAtualizarStatus")
  @PutMapping("/{id}/status")
  void atualizarStatus(@PathVariable("id") Long pedidoId, @RequestParam("status") String status);

  default void fallbackAtualizarStatus(Long pedidoId, String status, Throwable throwable) {
    PedidoClient.logger.warn("⚠️ Erro ao atualizar status do pedido {} - Fallback ativado: {}",
        pedidoId, throwable.getMessage());
  }

}
