package com.rsdesenvolvimento.estoque.modelo.entidade;

import com.rsdesenvolvimento.estoque.core.modelo.entidade.EntidadeBase;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Produto extends EntidadeBase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nome;
  private String descricao;
  private BigDecimal preco;
  private String categoria;
  private Integer estoque;

  private String imagemUrl;
}
