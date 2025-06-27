package com.rsdesenvolvimento.estoque.core.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
@Slf4j
public class CaffeineCache {

    @PostConstruct
    public void init() {
        CaffeineCache.log.info("[CAFFEINE CACHE]: Caffeine cache inicializado..");
    }



    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.registerCustomCache("estoque",

                Caffeine.newBuilder().initialCapacity(100).expireAfterWrite(10, TimeUnit.MINUTES)
                        .maximumSize(500).recordStats().build());

        return cacheManager;
    }
}
