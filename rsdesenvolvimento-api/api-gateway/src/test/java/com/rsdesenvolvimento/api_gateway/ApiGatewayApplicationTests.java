package com.rsdesenvolvimento.api_gateway;

import com.rsdesenvolvimento.api_gateway.config.CaffeineCache;
import com.rsdesenvolvimento.api_gateway.config.SecurityConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ApiGatewayApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(this.applicationContext);
    }

    @Test
    void testSecurityConfigBeanExists() {
        Assertions.assertTrue(this.applicationContext.containsBean("securityConfig"));
        SecurityConfig securityConfig = this.applicationContext.getBean(SecurityConfig.class);
        Assertions.assertNotNull(securityConfig);
    }

    @Test
    void testCaffeineCacheBeanExists() {
        Assertions.assertTrue(this.applicationContext.containsBean("caffeineCache"));
        CaffeineCache caffeineCache = this.applicationContext.getBean(CaffeineCache.class);
        Assertions.assertNotNull(caffeineCache);
    }

    @Test
    void testCacheManagerBeanExists() {
        CacheManager cacheManager = this.applicationContext.getBean(CacheManager.class);
        Assertions.assertNotNull(cacheManager);
    }

    @Test
    void testCorsConfigurationSourceBeanExists() {
        Assertions.assertTrue(this.applicationContext.containsBean("corsConfigurationSource"));
    }

    @Test
    void testSecurityFilterChainBeanExists() {
        Assertions.assertTrue(this.applicationContext.containsBean("securityFilterChain"));
    }

    @Test
    void testCaffeineBeanExists() {
        Assertions.assertTrue(this.applicationContext.containsBean("caffeineConfig"));
    }

    @Test
    void testApplicationStartsSuccessfully() {

        Assertions.assertNotNull(this.applicationContext.getBean(SecurityConfig.class));
        Assertions.assertNotNull(this.applicationContext.getBean(CaffeineCache.class));
        Assertions.assertNotNull(this.applicationContext.getBean(CacheManager.class));

        String[] beanNames = this.applicationContext.getBeanDefinitionNames();
        Assertions.assertTrue(beanNames.length > 0);

        Assertions.assertTrue(this.applicationContext.containsBean(
                "org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration"));
    }
}
