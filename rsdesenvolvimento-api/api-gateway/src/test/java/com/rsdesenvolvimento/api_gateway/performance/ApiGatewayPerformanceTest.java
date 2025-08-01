package com.rsdesenvolvimento.api_gateway.performance;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ApiGatewayPerformanceTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CacheManager cacheManager;

    @Test
    void testConcurrentRequests() {

        Flux<String> responses = Flux.range(1, 50)
                .flatMap(i -> this.webTestClient.get().uri("/actuator/health").exchange()
                        .expectStatus().isOk().returnResult(String.class).getResponseBody().next()
                        .onErrorReturn("error"), 10);


        String[] results = responses.collectList().block().toArray(new String[0]);
        Assertions.assertEquals(50, results.length);
    }

    @Test
    void testResponseTime() {

        this.webTestClient.mutate().responseTimeout(Duration.ofSeconds(5)).build().get()
                .uri("/actuator/health").exchange().expectStatus().isOk().expectBody()
                .jsonPath("$.status").isEqualTo("UP");
    }

    @Test
    void testCachePerformance() {
        org.springframework.cache.Cache cache = this.cacheManager.getCache("testCache");
        Assertions.assertNotNull(cache);


        long startTime = System.currentTimeMillis();


        for (int i = 0; i < 1000; i++) {
            cache.put("key" + i, "value" + i);
        }

        for (int i = 0; i < 1000; i++) {
            cache.get("key" + i);
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        Assertions.assertTrue(duration < 1000,
                "Cache operations took too long: " + duration + "ms");
    }

    @Test
    void testMemoryUsage() {

        Runtime runtime = Runtime.getRuntime();
        long initialMemory = runtime.totalMemory() - runtime.freeMemory();

        for (int i = 0; i < 100; i++) {
            this.webTestClient.get().uri("/actuator/health").exchange().expectStatus().isOk();
        }

        System.gc();

        long finalMemory = runtime.totalMemory() - runtime.freeMemory();
        long memoryIncrease = finalMemory - initialMemory;

        Assertions.assertTrue(memoryIncrease < 10 * 1024 * 1024,
                "Memory usage increased too much: " + memoryIncrease + " bytes");
    }

    @Test
    void testHighVolumeRequests() {
        ExecutorService executor = Executors.newFixedThreadPool(10);

        @SuppressWarnings("unchecked")
        CompletableFuture<Void>[] futures =
                IntStream.range(0, 100).mapToObj(i -> CompletableFuture.runAsync(() -> {
                    this.webTestClient.get().uri("/actuator/health").exchange().expectStatus()
                            .isOk();
                }, executor)).toArray(CompletableFuture[]::new);

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures);

        Assertions.assertDoesNotThrow(() -> {
            allFutures.get(java.util.concurrent.TimeUnit.SECONDS.toSeconds(30),
                    java.util.concurrent.TimeUnit.SECONDS);
        });

        executor.shutdown();
    }

    @Test
    void testCorsOverhead() {

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 50; i++) {
            this.webTestClient.options().uri("/test-endpoint")
                    .header("Origin", "http://localhost:4200")
                    .header("Access-Control-Request-Method", "GET").exchange().expectStatus()
                    .isOk();
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        Assertions.assertTrue(duration < 2000, "CORS requests took too long: " + duration + "ms");
    }

    @Test
    void testGatewayLatency() {
        int requestCount = 20;
        long totalTime = 0;

        for (int i = 0; i < requestCount; i++) {
            long requestStart = System.currentTimeMillis();

            this.webTestClient.get().uri("/actuator/health").exchange().expectStatus().isOk();

            long requestEnd = System.currentTimeMillis();
            totalTime += (requestEnd - requestStart);
        }

        double averageLatency = (double) totalTime / requestCount;

        Assertions.assertTrue(averageLatency < 200,
                "Average latency too high: " + averageLatency + "ms");
    }

    @Test
    void testReactiveStreamProcessing() {
        Flux<Integer> stream =
                Flux.range(1, 1000).map(i -> i * 2).filter(i -> i % 4 == 0).take(100);

        java.util.List<Integer> results = stream.collectList().block();
        Assertions.assertNotNull(results);
        Assertions.assertEquals(100, results.size());
    }

    @Test
    void testBackpressure() {
        Flux<Integer> slowProducer = Flux.range(1, 1000).delayElements(Duration.ofMillis(1));

        Flux<Integer> fastConsumer = slowProducer.onBackpressureBuffer(100).take(50);

        java.util.List<Integer> results = fastConsumer.collectList().block();
        Assertions.assertNotNull(results);
        Assertions.assertEquals(50, results.size());
    }
}
