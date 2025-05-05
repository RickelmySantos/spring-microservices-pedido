package com.rsdesenvolvimento.pedido_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.rsdesenvolvimento.pedido_service.core.client")
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
public class PedidoServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(PedidoServiceApplication.class, args);
  }

}
