package com.example.cqs.maintenance;

import com.example.cqs.service.BookQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;

@Component
public class MaintenanceTasks {
    private static final Logger log = LoggerFactory.getLogger(MaintenanceTasks.class);
    private final BookQueryService queryService;
    private final ThreadPoolTaskExecutor executor;

    public MaintenanceTasks(BookQueryService queryService,
                            @org.springframework.beans.factory.annotation.Qualifier("appTaskExecutor") ThreadPoolTaskExecutor executor) {
        this.queryService = queryService;
        this.executor = executor;
    }

    // Warm caches periodically to improve tail latency under burst load
    @Scheduled(fixedRateString = "60000") // every 60s
    public void warmCaches() {
        try {
            int size = queryService.getAll().size();
            log.debug("[SCHEDULED] Cache warmup completed: book-all size={}", size);
        } catch (Exception e) {
            log.warn("[SCHEDULED] Cache warmup failed: {}", e.getMessage());
        }
    }

    // Log executor stats for observability
    @Scheduled(fixedRateString = "30000") // every 30s
    public void logExecutorStats() {
        ThreadPoolExecutor tpe = executor.getThreadPoolExecutor();
        if (tpe != null) {
            log.debug("[EXECUTOR] poolSize={}, active={}, queued={}, completed={}, largest={}",
                    tpe.getPoolSize(), tpe.getActiveCount(), tpe.getQueue().size(), tpe.getCompletedTaskCount(), tpe.getLargestPoolSize());
        }
    }
}
