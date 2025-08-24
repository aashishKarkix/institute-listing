package com.institute.listing.core.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class InMemoryCacheConfig {

    /**
     * In-memory cache for storing sentiment analysis results.
     * Currently, using Caffeine for fast, local caching.
     * - Maximum of 100,000 review entries cached to prevent excessive memory usage.
     * - Each entry expires 7 days after being written.
     * Note: This cache is per application instance and will be cleared if the server restarts.
     * Later, we can integrate Redis as a distributed cache for:
     *   1. Sharing cache across multiple instances.
     *   2. Persisting cache beyond server restarts.
     *   3. Better memory management and eviction policies.
     **/
    @Bean(name = "sentimentCache")
    public Cache<String, String> sentimentCache() {
        return Caffeine.newBuilder()
                .maximumSize(100_000)
                .expireAfterWrite(7, TimeUnit.DAYS)
                .build();
    }


    /**
     * In-memory cache for storing comparisonCache analysis results.
     * Currently, using Caffeine for fast, local caching.
     * - Maximum of 100,000 review entries cached to prevent excessive memory usage.
     * - Each entry expires 1 day after being written.
     * Note: This cache is per application instance and will be cleared if the server restarts.
     **/
    @Bean(name = "comparisonCache")
    public Cache<String, Integer> comparisonCache() {
        return Caffeine.newBuilder()
                .maximumSize(10_000)
                .expireAfterWrite(1, TimeUnit.DAYS)
                .build();
    }
}

