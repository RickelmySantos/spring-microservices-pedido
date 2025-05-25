package com.rsdesenvolvimento.usuario.core.auditoria;

import org.hibernate.envers.RevisionListener;

public class AppRevisionListener implements RevisionListener {

  @Override
  public void newRevision(Object revisionEntity) {
    AppAuditRevision revision = (AppAuditRevision) revisionEntity;
    revision.setUsuario("USUARIO_MS");
  }

}
