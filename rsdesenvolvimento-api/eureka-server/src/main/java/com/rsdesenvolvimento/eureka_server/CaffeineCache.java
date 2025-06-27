package com.rsdesenvolvimento.eureka_server;

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
    public CacheManager cacheManager(Caffeine<Object, Object> caffeine) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(caffeine);
        return cacheManager;
    }

    @Bean
    public Caffeine<Object, Object> caffeineConfig() {
        return Caffeine.newBuilder().initialCapacity(100).maximumSize(500)
                .expireAfterWrite(10, TimeUnit.MINUTES).recordStats();
    }
}
