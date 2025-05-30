package com.rsdesenvolvimento.estoque.core.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.api.FlywayException;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Slf4j
public class FlywayRepairConfig {

    @PostConstruct
    public void init() {
        FlywayRepairConfig.log.debug("LOADED >>>> FlywayAutoConfig");
    }

    @Bean
    @Profile("!test & !deploy")
    public FlywayMigrationStrategy developmentConfig() {
        return flyway -> {
            try {
                flyway.migrate();
            } catch (FlywayException e) {
                FlywayRepairConfig.log.warn("Erro ao executar migrations, tentando reparar...");
                flyway.repair();
                flyway.migrate();
            }
        };
    }
}
