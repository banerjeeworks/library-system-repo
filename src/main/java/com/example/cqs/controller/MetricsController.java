package com.example.cqs.controller;

import com.github.benmanes.caffeine.cache.stats.CacheStats;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

@RestController
@RequestMapping("/api/metrics")
@PreAuthorize("hasRole('ADMIN')")
public class MetricsController {
    private final ThreadPoolTaskExecutor executor;
    private final CacheManager cacheManager;

    public MetricsController(@org.springframework.beans.factory.annotation.Qualifier("appTaskExecutor") ThreadPoolTaskExecutor executor,
                             CacheManager cacheManager) {
        this.executor = executor;
        this.cacheManager = cacheManager;
    }

    @GetMapping("/executor")
    public ResponseEntity<Map<String, Object>> executorMetrics() {
        ThreadPoolExecutor tpe = executor.getThreadPoolExecutor();
        Map<String, Object> map = new HashMap<>();
        if (tpe != null) {
            map.put("poolSize", tpe.getPoolSize());
            map.put("activeCount", tpe.getActiveCount());
            map.put("queueSize", tpe.getQueue().size());
            map.put("completedTaskCount", tpe.getCompletedTaskCount());
            map.put("largestPoolSize", tpe.getLargestPoolSize());
            map.put("corePoolSize", tpe.getCorePoolSize());
            map.put("maxPoolSize", tpe.getMaximumPoolSize());
        }
        return ResponseEntity.ok(map);
    }

    @GetMapping("/cache")
    public ResponseEntity<Map<String, Object>> cacheMetrics() {
        Map<String, Object> map = new HashMap<>();
        for (String name : cacheManager.getCacheNames()) {
            Cache cache = cacheManager.getCache(name);
            if (cache instanceof CaffeineCache caffeineCache) {
                CacheStats stats = caffeineCache.getNativeCache().stats();
                Map<String, Object> s = new HashMap<>();
                s.put("hitCount", stats.hitCount());
                s.put("missCount", stats.missCount());
                s.put("requestCount", stats.requestCount());
                s.put("hitRate", stats.hitRate());
                s.put("evictionCount", stats.evictionCount());
                map.put(name, s);
            }
        }
        return ResponseEntity.ok(map);
    }
}
