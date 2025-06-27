package com.rsdesenvolvimento.estoque;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@EnableCaching
public class EstoqueApplication {

    public static void main(String[] args) {
        SpringApplication.run(EstoqueApplication.class, args);
    }

}
