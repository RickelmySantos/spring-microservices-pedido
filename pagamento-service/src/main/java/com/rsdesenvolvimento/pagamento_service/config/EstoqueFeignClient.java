package com.rsdesenvolvimento.pagamento_service.config;

import com.rsdesenvolvimento.pagamento_service.modelo.dtos.ReservaEstoqueRequestDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "estoque-api", path = "/api/estoque")
public interface EstoqueFeignClient {

  @PostMapping("/validar")
  Boolean validarEstoque(@RequestBody List<ReservaEstoqueRequestDto> itens);

  @PostMapping("/reservar")
  void reservarEstoque(@RequestBody List<ReservaEstoqueRequestDto> itens);

}
