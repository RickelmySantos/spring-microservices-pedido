package com.rsdesenvolvimento.pedido_service.modelo.entidades;

import com.rsdesenvolvimento.pedido_service.core.modelo.EntidadeBase;
import com.rsdesenvolvimento.pedido_service.modelo.enums.StatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

@Entity
@Getter
@Setter
@ToString
@AuditOverride(forClass = EntidadeBase.class)
@Audited
public class Pedido extends EntidadeBase {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pedido_seq")
  @SequenceGenerator(name = "pedido_seq", sequenceName = "pedido_seq", allocationSize = 1)
  private Long id;

  private Long usuarioId;
  @Column(nullable = false)
  private String descricao;
  private String nomeUsuario;
  private String emailUsuario;
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, name = "status_pedido")
  private StatusEnum status;


}
