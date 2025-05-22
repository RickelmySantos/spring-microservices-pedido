package com.rsdesenvolvimento.pagamento_service.modelo.entidades;

import com.rsdesenvolvimento.pagamento_service.core.modelo.entidade.EntidadeBase;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

@Entity
@Getter
@Setter
@Audited
@AuditOverride(forClass = EntidadeBase.class)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pagamento extends EntidadeBase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long pedidoId;
  private BigDecimal valor;
  private LocalDateTime dataHora;
  private String status;
}
