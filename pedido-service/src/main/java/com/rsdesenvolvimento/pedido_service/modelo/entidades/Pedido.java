package com.rsdesenvolvimento.pedido_service.modelo.entidades;

import com.rsdesenvolvimento.pedido_service.core.modelo.EntidadeBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Pedido extends EntidadeBase {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pedido_seq")
  @SequenceGenerator(name = "pedido_seq", sequenceName = "pedido_seq", allocationSize = 1)
  private Long id;

  private Long usuarioId;
  @Column(nullable = false)
  private String descricao;


}
