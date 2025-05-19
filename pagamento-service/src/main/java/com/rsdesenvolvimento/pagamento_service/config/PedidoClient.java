package com.rsdesenvolvimento.pagamento_service.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "pedido-service", path = "/api/pedido")
public interface PedidoClient {
  @PutMapping("/{id}/status")
  void atualizarStatus(@PathVariable("id") Long pedidoId, @RequestParam("status") String status);
}
