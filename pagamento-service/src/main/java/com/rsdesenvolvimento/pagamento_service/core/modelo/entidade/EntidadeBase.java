package com.rsdesenvolvimento.pagamento_service.core.modelo.entidade;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


/**
 * EntidadeBase
 *
 * @author rsdesenvolvimento
 * @version 1.0
 * @since 2025-04-29
 */
@Getter
@Setter
@ToString
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class EntidadeBase {


  @CreatedDate
  @Column(updatable = false)
  private LocalDateTime dataHoraCriacao;

  @CreatedBy
  @Column(updatable = false)
  private String criadoPor;

  @LastModifiedDate
  @Column(insertable = false)
  private LocalDateTime dataHoraAtualizacao;

  @LastModifiedBy
  @Column(insertable = false)
  private String atualizadoPor;
}
