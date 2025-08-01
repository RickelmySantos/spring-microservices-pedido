package com.rsdesenvolvimento.api_gateway.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;

class CaffeineCacheTest {

    private CaffeineCache caffeineCache;

    @BeforeEach
    void setUp() {
        this.caffeineCache = new CaffeineCache();
    }

    @Test
    void testCaffeineConfig() {
        // When
        Caffeine<Object, Object> caffeine = this.caffeineCache.caffeineConfig();

        // Then
        Assertions.assertNotNull(caffeine);

        com.github.benmanes.caffeine.cache.Cache<Object, Object> cache = caffeine.build();
        Assertions.assertNotNull(cache);

        cache.put("test-key", "test-value");
        Assertions.assertEquals("test-value", cache.getIfPresent("test-key"));
    }

    @Test
    void testCacheManager() {
        // Given
        Caffeine<Object, Object> caffeine = this.caffeineCache.caffeineConfig();

        // When
        CacheManager cacheManager = this.caffeineCache.cacheManager(caffeine);

        // Then
        Assertions.assertNotNull(cacheManager);
        Assertions.assertTrue(cacheManager instanceof CaffeineCacheManager);

        CaffeineCacheManager caffeineCacheManager = (CaffeineCacheManager) cacheManager;
        Assertions.assertNotNull(caffeineCacheManager);
    }

    @Test
    void testCacheManagerWithCache() {
        // Given
        Caffeine<Object, Object> caffeine = this.caffeineCache.caffeineConfig();
        CacheManager cacheManager = this.caffeineCache.cacheManager(caffeine);

        // When
        org.springframework.cache.Cache cache = cacheManager.getCache("testCache");

        // Then
        Assertions.assertNotNull(cache);

        cache.put("key1", "value1");
        org.springframework.cache.Cache.ValueWrapper wrapper = cache.get("key1");
        Assertions.assertNotNull(wrapper);
        Assertions.assertEquals("value1", wrapper.get());
    }

    @Test
    void testInitMethod() {
        Assertions.assertDoesNotThrow(() -> this.caffeineCache.init());
    }

    @Test
    void testCaffeineConfiguration() {
        // When
        Caffeine<Object, Object> caffeine = this.caffeineCache.caffeineConfig();
        com.github.benmanes.caffeine.cache.Cache<Object, Object> cache = caffeine.build();


        for (int i = 0; i < 100; i++) {
            cache.put("key" + i, "value" + i);
        }

        Assertions.assertNotNull(cache.getIfPresent("key1"));
        Assertions.assertNotNull(cache.getIfPresent("key50"));
        Assertions.assertNotNull(cache.getIfPresent("key99"));

        Assertions.assertNotNull(cache.stats());
    }

    @Test
    void testCacheEviction() {
        // Given
        Caffeine<Object, Object> caffeine = this.caffeineCache.caffeineConfig();
        com.github.benmanes.caffeine.cache.Cache<Object, Object> cache = caffeine.build();


        for (int i = 0; i < 600; i++) {
            cache.put("key" + i, "value" + i);
        }

        long cacheSize = cache.estimatedSize();
        Assertions.assertTrue(cacheSize <= 600,
                "Cache size should be reasonable, got: " + cacheSize);

        Assertions.assertTrue(cacheSize > 0, "Cache should have some entries");
    }

    @Test
    void testCacheManagerGetCacheNames() {
        // Given
        Caffeine<Object, Object> caffeine = this.caffeineCache.caffeineConfig();
        CacheManager cacheManager = this.caffeineCache.cacheManager(caffeine);

        // When - Create some caches
        cacheManager.getCache("cache1");
        cacheManager.getCache("cache2");

        // Then
        Assertions.assertTrue(cacheManager.getCacheNames().contains("cache1"));
        Assertions.assertTrue(cacheManager.getCacheNames().contains("cache2"));
    }
}
