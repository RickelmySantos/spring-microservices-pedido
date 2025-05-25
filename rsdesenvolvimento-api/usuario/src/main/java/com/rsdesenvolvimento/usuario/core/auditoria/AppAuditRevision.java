package com.rsdesenvolvimento.usuario.core.auditoria;

import jakarta.persistence.Entity;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import org.hibernate.envers.RevisionEntity;

@Entity
@Table(name = AuditRevision.TABLE_NAME, schema = AppAuditRevision.APP_AUDIT_SCHEMA_NAME)
@SequenceGenerator(name = AuditRevision.SEQUENCE_GENERATOR,
    sequenceName = AuditRevision.SEQUENCE_NAME, initialValue = 1, allocationSize = 1,
    schema = AppAuditRevision.APP_AUDIT_SCHEMA_NAME)
@RevisionEntity(AppRevisionListener.class)
public class AppAuditRevision extends AuditRevision {
  static final String APP_AUDIT_SCHEMA_NAME = "usuario_aud";
}
