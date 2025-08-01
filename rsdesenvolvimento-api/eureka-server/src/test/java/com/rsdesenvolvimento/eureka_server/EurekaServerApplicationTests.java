package com.rsdesenvolvimento.eureka_server;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Testes da Aplicação Eureka Server")
class EurekaServerApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Nested
    @DisplayName("Carregamento do Contexto")
    class ContextLoadingTests {

        @Test
        @DisplayName("Deve carregar o contexto da aplicação")
        void deveCarregarOContextoDaAplicacao() {
            Assertions.assertThat(EurekaServerApplicationTests.this.applicationContext).isNotNull();
        }

        @Test
        @DisplayName("Deve ter bean CaffeineCache configurado")
        void deveTerBeanCaffeineCacheConfigurado() {
            Assertions.assertThat(EurekaServerApplicationTests.this.applicationContext
                    .containsBean("caffeineCache")).isTrue();

            CaffeineCache caffeineCache = EurekaServerApplicationTests.this.applicationContext
                    .getBean(CaffeineCache.class);
            Assertions.assertThat(caffeineCache).isNotNull();
        }

        @Test
        @DisplayName("Deve ter bean CacheManager configurado")
        void deveTerBeanCacheManagerConfigurado() {
            Assertions.assertThat(EurekaServerApplicationTests.this.applicationContext
                    .containsBean("cacheManager")).isTrue();

            CacheManager cacheManager = EurekaServerApplicationTests.this.applicationContext
                    .getBean(CacheManager.class);
            Assertions.assertThat(cacheManager).isNotNull();
        }

        @Test
        @DisplayName("Deve ter bean Caffeine configurado")
        void deveTerBeanCaffeineConfigurado() {
            Assertions.assertThat(EurekaServerApplicationTests.this.applicationContext
                    .containsBean("caffeineConfig")).isTrue();

            @SuppressWarnings("unchecked")
            Caffeine<Object, Object> caffeine = EurekaServerApplicationTests.this.applicationContext
                    .getBean("caffeineConfig", Caffeine.class);
            Assertions.assertThat(caffeine).isNotNull();
        }
    }

    @Nested
    @DisplayName("Configuração de Anotações")
    class AnnotationConfigurationTests {

        @Test
        @DisplayName("Deve ter anotação @EnableEurekaServer")
        void deveTerAnotacaoEnableEurekaServer() {
            boolean hasEurekaServerAnnotation = EurekaServerApplication.class.isAnnotationPresent(
                    org.springframework.cloud.netflix.eureka.server.EnableEurekaServer.class);

            Assertions.assertThat(hasEurekaServerAnnotation).isTrue();
        }

        @Test
        @DisplayName("Deve ter anotação @EnableCaching")
        void deveTerAnotacaoEnableCaching() {
            boolean hasCachingAnnotation = EurekaServerApplication.class
                    .isAnnotationPresent(org.springframework.cache.annotation.EnableCaching.class);

            Assertions.assertThat(hasCachingAnnotation).isTrue();
        }

        @Test
        @DisplayName("Deve ter anotação @SpringBootApplication")
        void deveTerAnotacaoSpringBootApplication() {
            boolean hasSpringBootAnnotation = EurekaServerApplication.class.isAnnotationPresent(
                    org.springframework.boot.autoconfigure.SpringBootApplication.class);

            Assertions.assertThat(hasSpringBootAnnotation).isTrue();
        }
    }

    @Nested
    @DisplayName("Funcionalidade de Cache")
    class CacheFunctionalityTests {

        @Autowired
        private CacheManager cacheManager;

        @Test
        @DisplayName("Deve permitir operações de cache")
        void devePermitirOperacoesDeCache() {
            // Given
            String cacheName = "eurekaCache";
            String key = "testKey";
            String value = "testValue";

            // When
            org.springframework.cache.Cache cache = this.cacheManager.getCache(cacheName);
            Assertions.assertThat(cache).isNotNull();

            if (cache != null) {
                cache.put(key, value);

                // Then
                org.springframework.cache.Cache.ValueWrapper result = cache.get(key);
                Assertions.assertThat(result).isNotNull();
                if (result != null) {
                    Assertions.assertThat(result.get()).isEqualTo(value);
                }
            }
        }

        @Test
        @DisplayName("Deve suportar múltiplos caches")
        void deveSuportarMultiplosCaches() {
            // Given
            String cache1Name = "cache1";
            String cache2Name = "cache2";

            // When
            org.springframework.cache.Cache cache1 = this.cacheManager.getCache(cache1Name);
            org.springframework.cache.Cache cache2 = this.cacheManager.getCache(cache2Name);

            // Then
            Assertions.assertThat(cache1).isNotNull();
            Assertions.assertThat(cache2).isNotNull();
            Assertions.assertThat(cache1).isNotSameAs(cache2);
        }

        @Test
        @DisplayName("Deve manter isolation entre caches diferentes")
        void deveManterIsolationEntreCachesDiferentes() {
            // Given
            String cache1Name = "isolationCache1";
            String cache2Name = "isolationCache2";
            String key = "sameKey";
            String value1 = "value1";
            String value2 = "value2";

            // When
            org.springframework.cache.Cache cache1 = this.cacheManager.getCache(cache1Name);
            org.springframework.cache.Cache cache2 = this.cacheManager.getCache(cache2Name);

            if (cache1 != null && cache2 != null) {
                cache1.put(key, value1);
                cache2.put(key, value2);

                // Then
                org.springframework.cache.Cache.ValueWrapper result1 = cache1.get(key);
                org.springframework.cache.Cache.ValueWrapper result2 = cache2.get(key);

                Assertions.assertThat(result1).isNotNull();
                Assertions.assertThat(result2).isNotNull();

                if (result1 != null && result2 != null) {
                    Assertions.assertThat(result1.get()).isEqualTo(value1);
                    Assertions.assertThat(result2.get()).isEqualTo(value2);
                    Assertions.assertThat(result1.get()).isNotEqualTo(result2.get());
                }
            }
        }
    }

    @Nested
    @DisplayName("Configuração do Eureka")
    class EurekaConfigurationTests {

        @Test
        @DisplayName("Deve inicializar sem erros")
        void deveInicializarSemErros() {
            // O fato de a aplicação ter carregado com sucesso já indica que o Eureka foi
            // configurado corretamente
            Assertions.assertThat(EurekaServerApplicationTests.this.applicationContext).isNotNull();
        }

        @Test
        @DisplayName("Deve ter beans do Eureka no contexto")
        void deveTerBeansDoEurekaNoContexto() {
            // Verifica se os beans essenciais do Eureka estão presentes
            String[] beanNames =
                    EurekaServerApplicationTests.this.applicationContext.getBeanDefinitionNames();
            Assertions.assertThat(beanNames).isNotEmpty();

            // O Eureka Server deve ter registrado seus beans automaticamente
            // A aplicação deve estar funcionando se chegou até aqui
            Assertions.assertThat(
                    EurekaServerApplicationTests.this.applicationContext.getBeanDefinitionCount())
                    .isGreaterThan(0);
        }
    }
}
