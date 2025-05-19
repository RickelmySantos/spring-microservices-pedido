package com.rsdesenvolvimento.pagamento_service.core.auditoria;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;


@Component("auditAwareImpl")
public class AuditoriaConfig implements AuditorAware<String> {

  @Override
  public Optional<String> getCurrentAuditor() {
    return Optional.of("PAGAMENTO_MS");
  }
}
