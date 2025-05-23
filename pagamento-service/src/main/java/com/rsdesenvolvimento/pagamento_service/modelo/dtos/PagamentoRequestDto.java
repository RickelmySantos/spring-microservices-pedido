package com.rsdesenvolvimento.pagamento_service.modelo.dtos;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class PagamentoRequestDto {

  private Long pedidoId;
  private BigDecimal valor;
  private List<ReservaEstoqueRequestDto> itens;
}
