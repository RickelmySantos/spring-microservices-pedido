package com.rsdesenvolvimento.pagamento_service.modelo.dtos;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class PagamentoRequestDto {

  private Long pedidoId;
  private BigDecimal valor;
}
