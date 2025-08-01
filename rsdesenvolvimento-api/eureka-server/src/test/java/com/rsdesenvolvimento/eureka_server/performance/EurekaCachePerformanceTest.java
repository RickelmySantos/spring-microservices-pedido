package com.rsdesenvolvimento.eureka_server.performance;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.rsdesenvolvimento.eureka_server.CaffeineCache;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@DisplayName("Testes de Performance do Cache Eureka")
class EurekaCachePerformanceTest {

    @InjectMocks
    private CaffeineCache caffeineCache;

    private CacheManager cacheManager;
    private org.springframework.cache.Cache cache;

    @BeforeEach
    void setUp() {
        Caffeine<Object, Object> caffeine = this.caffeineCache.caffeineConfig();
        this.cacheManager = this.caffeineCache.cacheManager(caffeine);
        this.cache = this.cacheManager.getCache("performanceTestCache");
    }

    @Nested
    @DisplayName("Performance de Operações Básicas")
    class BasicOperationsPerformanceTests {

        @Test
        @DisplayName("Deve inserir milhares de entradas rapidamente")
        void deveInserirMilharesDeEntradasRapidamente() {
            // Given
            int numberOfEntries = 10000;
            long startTime = System.currentTimeMillis();

            // When
            if (EurekaCachePerformanceTest.this.cache != null) {
                IntStream.range(0, numberOfEntries).forEach(
                        i -> EurekaCachePerformanceTest.this.cache.put("key" + i, "value" + i));

                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;

                // Then
                Assertions.assertThat(duration).isLessThan(5000);

                long presentEntries = IntStream.range(9000, numberOfEntries)
                        .mapToObj(i -> EurekaCachePerformanceTest.this.cache.get("key" + i))
                        .filter(value -> value != null).count();

                Assertions.assertThat(presentEntries).isGreaterThan(0);
            }
        }

        @Test
        @DisplayName("Deve recuperar entradas rapidamente")
        void deveRecuperarEntradasRapidamente() {
            // Given
            int numberOfEntries = 1000;

            if (EurekaCachePerformanceTest.this.cache != null) {
                // Popula o cache
                IntStream.range(0, numberOfEntries).forEach(
                        i -> EurekaCachePerformanceTest.this.cache.put("key" + i, "value" + i));

                long startTime = System.currentTimeMillis();

                // When
                IntStream.range(0, numberOfEntries)
                        .forEach(i -> EurekaCachePerformanceTest.this.cache.get("key" + i));

                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;

                // Then
                Assertions.assertThat(duration).isLessThan(1000);
            }
        }

        @Test
        @DisplayName("Deve remover entradas rapidamente")
        void deveRemoverEntradasRapidamente() {
            // Given
            int numberOfEntries = 1000;

            if (EurekaCachePerformanceTest.this.cache != null) {
                // Popula o cache
                IntStream.range(0, numberOfEntries).forEach(
                        i -> EurekaCachePerformanceTest.this.cache.put("key" + i, "value" + i));

                long startTime = System.currentTimeMillis();

                // When
                IntStream.range(0, numberOfEntries)
                        .forEach(i -> EurekaCachePerformanceTest.this.cache.evict("key" + i));

                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;

                // Then
                Assertions.assertThat(duration).isLessThan(1000);


                Assertions.assertThat(EurekaCachePerformanceTest.this.cache.get("key100")).isNull();
                Assertions.assertThat(EurekaCachePerformanceTest.this.cache.get("key500")).isNull();
                Assertions.assertThat(EurekaCachePerformanceTest.this.cache.get("key999")).isNull();
            }
        }
    }

    @Nested
    @DisplayName("Performance Concorrente")
    class ConcurrentPerformanceTests {

        @Test
        @DisplayName("Deve suportar escritas concorrentes")
        void deveSuportarEscritasConcorrentes() throws Exception {
            // Given
            int numberOfThreads = 10;
            int entriesPerThread = 100;
            ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

            long startTime = System.currentTimeMillis();

            // When
            CompletableFuture<?>[] futures = IntStream.range(0, numberOfThreads)
                    .mapToObj(threadId -> CompletableFuture.runAsync(() -> {
                        if (EurekaCachePerformanceTest.this.cache != null) {
                            IntStream.range(0, entriesPerThread)
                                    .forEach(i -> EurekaCachePerformanceTest.this.cache.put(
                                            "thread" + threadId + "_key" + i,
                                            "thread" + threadId + "_value" + i));
                        }
                    }, executor)).toArray(CompletableFuture[]::new);

            CompletableFuture.allOf(futures).get(10, TimeUnit.SECONDS);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            // Then
            Assertions.assertThat(duration).isLessThan(5000);

            if (EurekaCachePerformanceTest.this.cache != null) {

                long presentEntries = IntStream.of(0, 5, 9)
                        .mapToObj(threadId -> EurekaCachePerformanceTest.this.cache
                                .get("thread" + threadId + "_key10"))
                        .filter(value -> value != null).count();

                Assertions.assertThat(presentEntries).isGreaterThan(0);
            }

            executor.shutdown();
        }

        @Test
        @DisplayName("Deve suportar leituras concorrentes")
        void deveSuportarLeiturasConcorrentes() throws Exception {
            // Given
            int numberOfEntries = 100;
            int numberOfThreads = 20;

            if (EurekaCachePerformanceTest.this.cache != null) {

                IntStream.range(0, numberOfEntries).forEach(
                        i -> EurekaCachePerformanceTest.this.cache.put("key" + i, "value" + i));

                ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
                long startTime = System.currentTimeMillis();

                // When
                CompletableFuture<?>[] futures = IntStream.range(0, numberOfThreads)
                        .mapToObj(threadId -> CompletableFuture.runAsync(() -> {
                            IntStream.range(0, numberOfEntries).forEach(
                                    i -> EurekaCachePerformanceTest.this.cache.get("key" + i));
                        }, executor)).toArray(CompletableFuture[]::new);

                CompletableFuture.allOf(futures).get(10, TimeUnit.SECONDS);

                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;

                // Then
                Assertions.assertThat(duration).isLessThan(3000);

                executor.shutdown();
            }
        }

        @Test
        @DisplayName("Deve suportar operações mistas concorrentes")
        void deveSuportarOperacoesMistasConcorrentes() throws Exception {
            // Given
            int numberOfThreads = 6;
            ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

            if (EurekaCachePerformanceTest.this.cache != null) {
                long startTime = System.currentTimeMillis();

                // When
                CompletableFuture<?>[] futures = new CompletableFuture<?>[numberOfThreads];

                // Threads de escrita
                futures[0] =
                        CompletableFuture
                                .runAsync(
                                        () -> IntStream.range(0, 100)
                                                .forEach(i -> EurekaCachePerformanceTest.this.cache
                                                        .put("write1_" + i, "value" + i)),
                                        executor);
                futures[1] =
                        CompletableFuture
                                .runAsync(
                                        () -> IntStream.range(100, 200)
                                                .forEach(i -> EurekaCachePerformanceTest.this.cache
                                                        .put("write2_" + i, "value" + i)),
                                        executor);

                // Threads de leitura
                futures[2] = CompletableFuture.runAsync(
                        () -> IntStream.range(0, 50).forEach(
                                i -> EurekaCachePerformanceTest.this.cache.get("write1_" + i)),
                        executor);
                futures[3] = CompletableFuture.runAsync(
                        () -> IntStream.range(100, 150).forEach(
                                i -> EurekaCachePerformanceTest.this.cache.get("write2_" + i)),
                        executor);

                // Threads de remoção
                futures[4] = CompletableFuture.runAsync(
                        () -> IntStream.range(0, 25).forEach(
                                i -> EurekaCachePerformanceTest.this.cache.evict("write1_" + i)),
                        executor);
                futures[5] = CompletableFuture.runAsync(
                        () -> IntStream.range(100, 125).forEach(
                                i -> EurekaCachePerformanceTest.this.cache.evict("write2_" + i)),
                        executor);

                CompletableFuture.allOf(futures).get(15, TimeUnit.SECONDS);

                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;

                // Then
                Assertions.assertThat(duration).isLessThan(10000);

                executor.shutdown();
            }
        }
    }

    @Nested
    @DisplayName("Validação de Limites")
    class LimitValidationTests {

        @Test
        @DisplayName("Deve respeitar limite máximo de entries")
        void deveRespeitarLimiteMaximoDeEntries() {
            // Given
            int maxEntries = 500;
            int entriesToAdd = 600;

            if (EurekaCachePerformanceTest.this.cache != null) {
                // When
                IntStream.range(0, entriesToAdd).forEach(i -> EurekaCachePerformanceTest.this.cache
                        .put("limitTest_" + i, "value" + i));

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                // Then

                long presentEntries = IntStream.range(0, entriesToAdd)
                        .mapToObj(i -> EurekaCachePerformanceTest.this.cache.get("limitTest_" + i))
                        .filter(value -> value != null).count();


                Assertions.assertThat(presentEntries).isLessThanOrEqualTo(maxEntries + 10);
            }
        }

        @Test
        @DisplayName("Deve ter performance consistente com cache cheio")
        void deveTerPerformanceConsistenteComCacheCheio() {
            // Given
            int cacheSize = 500;

            if (EurekaCachePerformanceTest.this.cache != null) {

                IntStream.range(0, cacheSize).forEach(
                        i -> EurekaCachePerformanceTest.this.cache.put("full_" + i, "value" + i));

                long startTime = System.currentTimeMillis();

                // When -
                IntStream.range(cacheSize, cacheSize + 100)
                        .forEach(i -> EurekaCachePerformanceTest.this.cache.put("overflow_" + i,
                                "value" + i));

                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;

                // Then
                Assertions.assertThat(duration).isLessThan(1000);
            }
        }
    }
}
