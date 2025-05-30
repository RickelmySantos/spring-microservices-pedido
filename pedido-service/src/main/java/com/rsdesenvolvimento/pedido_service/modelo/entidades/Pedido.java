package com.rsdesenvolvimento.pedido_service.modelo.entidades;

import com.rsdesenvolvimento.pedido_service.core.modelo.EntidadeBase;
import com.rsdesenvolvimento.pedido_service.modelo.enums.StatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Audited
@AuditOverride(forClass = EntidadeBase.class)
@Table(name = "TB_PEDIDO", uniqueConstraints = {})
@SequenceGenerator(name = EntidadeBase.SEQUENCE_GENERATOR, sequenceName = "SQ_PEDIDO",
    initialValue = 1, allocationSize = 1)
public class Pedido extends EntidadeBase {

  private static final long serialVersionUID = 1L;

  private Long usuarioId;
  @Column(nullable = false)
  private String descricao;
  @Column(name = "NOME_USUARIO", nullable = false, length = 100)
  private String nomeUsuario;
  @Column(name = "EMAIL_USUARIO", nullable = false, length = 50)
  @Email
  private String emailUsuario;
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, name = "status_pedido")
  private StatusEnum status;
}
