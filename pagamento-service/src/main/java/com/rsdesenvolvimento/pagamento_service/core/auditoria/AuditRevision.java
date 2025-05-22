package com.rsdesenvolvimento.pagamento_service.core.auditoria;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

@FieldNameConstants
@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class AuditRevision {


  public static final String SEQUENCE_GENERATOR = "revinfoSeq";
  public static final String SEQUENCE_NAME = "revinfo_seq";
  public static final String TABLE_NAME = "revinfo";

  @Id
  @GeneratedValue(generator = AuditRevision.SEQUENCE_GENERATOR, strategy = GenerationType.SEQUENCE)
  @RevisionNumber
  private int id;

  @RevisionTimestamp
  private long timestamp;

  @Column(name = "pagamento")
  private String pagamento;

}
