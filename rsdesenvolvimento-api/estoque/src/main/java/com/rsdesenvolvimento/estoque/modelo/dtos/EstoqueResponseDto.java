package com.rsdesenvolvimento.estoque.modelo.dtos;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class EstoqueResponseDto {

  private Long id;
  private String nome;
  private String descricao;
  private BigDecimal preco;
  private String categoria;
  private Integer estoque;
  private String imagemUrl;
}
