package com.rsdesenvolvimento.pedido_service.core.auditoria;

import org.hibernate.envers.RevisionListener;

public class AppRevisionListener implements RevisionListener {

  @Override
  public void newRevision(Object revisionEntity) {
    AppAuditRevision revision = (AppAuditRevision) revisionEntity;
    revision.setPedido("PEDIDO_MS");
  }

}
