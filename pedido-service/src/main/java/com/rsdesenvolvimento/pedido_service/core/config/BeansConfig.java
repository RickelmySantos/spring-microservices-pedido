package com.rsdesenvolvimento.pedido_service.core.config;

import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfig {

  @Bean
  public Retryer feignRetryer() {
    return Retryer.NEVER_RETRY;
  }
}
