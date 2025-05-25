package com.rsdesenvolvimento.pagamento_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@EnableFeignClients(basePackages = "com.rsdesenvolvimento.pagamento_service.config")
public class PagamentoServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(PagamentoServiceApplication.class, args);
  }

}
