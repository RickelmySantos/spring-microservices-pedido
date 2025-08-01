package com.rsdesenvolvimento.eureka_server.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.rsdesenvolvimento.eureka_server.CaffeineCache;
import java.util.concurrent.TimeUnit;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@DisplayName("Testes de Configuração do Cache Caffeine")
class CaffeineCacheTest {

    @InjectMocks
    private CaffeineCache caffeineCache;

    @Nested
    @DisplayName("Configuração do Caffeine")
    class CaffeineConfigurationTests {

        @Test
        @DisplayName("Deve criar configuração Caffeine com parâmetros corretos")
        void deveCriarConfiguracaoCaffeineComParametrosCorretos() {
            // When
            Caffeine<Object, Object> caffeine =
                    CaffeineCacheTest.this.caffeineCache.caffeineConfig();

            // Then
            Assertions.assertThat(caffeine).isNotNull();

            com.github.benmanes.caffeine.cache.Cache<Object, Object> cache = caffeine.build();
            Assertions.assertThat(cache).isNotNull();
        }

        @Test
        @DisplayName("Deve configurar capacidade inicial correta")
        void deveConfigurarCapacidadeInicialCorreta() {
            // When
            Caffeine<Object, Object> caffeine =
                    CaffeineCacheTest.this.caffeineCache.caffeineConfig();

            // Then
            Assertions.assertThat(caffeine).isNotNull();
            String configString = caffeine.toString();
            Assertions.assertThat(configString).contains("initialCapacity=100");
        }

        @Test
        @DisplayName("Deve configurar tamanho máximo correto")
        void deveConfigurarTamanhoMaximoCorreto() {
            // When
            Caffeine<Object, Object> caffeine =
                    CaffeineCacheTest.this.caffeineCache.caffeineConfig();

            // Then
            Assertions.assertThat(caffeine).isNotNull();
            String configString = caffeine.toString();
            Assertions.assertThat(configString).contains("maximumSize=500");
        }

        @Test
        @DisplayName("Deve configurar expiração após escrita")
        void deveConfigurarExpiracaoAposEscrita() {
            // When
            Caffeine<Object, Object> caffeine =
                    CaffeineCacheTest.this.caffeineCache.caffeineConfig();

            // Then
            Assertions.assertThat(caffeine).isNotNull();
            String configString = caffeine.toString();
            Assertions.assertThat(configString).contains("expireAfterWrite=600000000000ns");
        }

        @Test
        @DisplayName("Deve habilitar estatísticas de cache")
        void deveHabilitarEstatisticasDeCache() {
            // When
            Caffeine<Object, Object> caffeine =
                    CaffeineCacheTest.this.caffeineCache.caffeineConfig();
            com.github.benmanes.caffeine.cache.Cache<Object, Object> cache = caffeine.build();

            // Then
            Assertions.assertThat(caffeine).isNotNull();
            Assertions.assertThat(cache.stats()).isNotNull();
            Assertions.assertThat(cache.stats().hitCount()).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("Cache Manager")
    class CacheManagerTests {

        private Caffeine<Object, Object> mockCaffeine;

        @BeforeEach
        void setUp() {
            this.mockCaffeine = Caffeine.newBuilder().initialCapacity(100).maximumSize(500)
                    .expireAfterWrite(10, TimeUnit.MINUTES).recordStats();
        }

        @Test
        @DisplayName("Deve criar CacheManager com configuração Caffeine")
        void deveCriarCacheManagerComConfiguracaoCaffeine() {
            // When
            CacheManager cacheManager =
                    CaffeineCacheTest.this.caffeineCache.cacheManager(this.mockCaffeine);

            // Then
            Assertions.assertThat(cacheManager).isNotNull();
            Assertions.assertThat(cacheManager).isInstanceOf(CaffeineCacheManager.class);
        }

        @Test
        @DisplayName("Deve aplicar configuração Caffeine ao CacheManager")
        void deveAplicarConfiguracaoCaffeineAoCacheManager() {
            // When
            CacheManager cacheManager =
                    CaffeineCacheTest.this.caffeineCache.cacheManager(this.mockCaffeine);
            CaffeineCacheManager caffeineCacheManager = (CaffeineCacheManager) cacheManager;

            // Then
            Assertions.assertThat(caffeineCacheManager).isNotNull();

            org.springframework.cache.Cache cache = caffeineCacheManager.getCache("testCache");
            Assertions.assertThat(cache).isNotNull();
        }
    }

    @Nested
    @DisplayName("Funcionalidade do Cache")
    class CacheFunctionalityTests {

        @Test
        @DisplayName("Deve armazenar e recuperar valores do cache")
        void deveArmazenarERecuperarValoresDoCache() {
            // Given
            Caffeine<Object, Object> caffeine =
                    CaffeineCacheTest.this.caffeineCache.caffeineConfig();
            CacheManager cacheManager = CaffeineCacheTest.this.caffeineCache.cacheManager(caffeine);

            // When
            org.springframework.cache.Cache cache = cacheManager.getCache("testCache");
            Assertions.assertThat(cache).isNotNull();

            if (cache != null) {
                cache.put("key1", "value1");

                // Then
                org.springframework.cache.Cache.ValueWrapper valueWrapper = cache.get("key1");
                Assertions.assertThat(valueWrapper).isNotNull();
                if (valueWrapper != null) {
                    Assertions.assertThat(valueWrapper.get()).isEqualTo("value1");
                }
            }
        }

        @Test
        @DisplayName("Deve retornar null para chaves não existentes")
        void deveRetornarNullParaChavesNaoExistentes() {
            // Given
            Caffeine<Object, Object> caffeine =
                    CaffeineCacheTest.this.caffeineCache.caffeineConfig();
            CacheManager cacheManager = CaffeineCacheTest.this.caffeineCache.cacheManager(caffeine);

            // When
            org.springframework.cache.Cache cache = cacheManager.getCache("testCache");
            Assertions.assertThat(cache).isNotNull();

            if (cache != null) {
                // Then
                Assertions.assertThat(cache.get("nonExistentKey")).isNull();
            }
        }

        @Test
        @DisplayName("Deve remover valores do cache")
        void deveRemoverValoresDoCache() {
            // Given
            Caffeine<Object, Object> caffeine =
                    CaffeineCacheTest.this.caffeineCache.caffeineConfig();
            CacheManager cacheManager = CaffeineCacheTest.this.caffeineCache.cacheManager(caffeine);
            org.springframework.cache.Cache cache = cacheManager.getCache("testCache");
            Assertions.assertThat(cache).isNotNull();

            if (cache != null) {
                // When
                cache.put("key1", "value1");
                Assertions.assertThat(cache.get("key1")).isNotNull();

                cache.evict("key1");

                // Then
                Assertions.assertThat(cache.get("key1")).isNull();
            }
        }

        @Test
        @DisplayName("Deve limpar todo o cache")
        void deveLimparTodoOCache() {
            // Given
            Caffeine<Object, Object> caffeine =
                    CaffeineCacheTest.this.caffeineCache.caffeineConfig();
            CacheManager cacheManager = CaffeineCacheTest.this.caffeineCache.cacheManager(caffeine);
            org.springframework.cache.Cache cache = cacheManager.getCache("testCache");
            Assertions.assertThat(cache).isNotNull();

            if (cache != null) {
                // When
                cache.put("key1", "value1");
                cache.put("key2", "value2");
                Assertions.assertThat(cache.get("key1")).isNotNull();
                Assertions.assertThat(cache.get("key2")).isNotNull();

                cache.clear();

                // Then
                Assertions.assertThat(cache.get("key1")).isNull();
                Assertions.assertThat(cache.get("key2")).isNull();
            }
        }
    }

    @Nested
    @DisplayName("Inicialização")
    class InitializationTests {

        @Test
        @DisplayName("Deve executar método de inicialização sem erro")
        void deveExecutarMetodoDeInicializacaoSemErro() {
            // When & Then
            org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                CaffeineCacheTest.this.caffeineCache.init();
            });
        }
    }
}
