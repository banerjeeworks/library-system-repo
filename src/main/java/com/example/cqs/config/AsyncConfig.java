package com.example.cqs.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;
import org.slf4j.MDC;

@Configuration
public class AsyncConfig {
    private static final Logger log = LoggerFactory.getLogger(AsyncConfig.class);

    @Bean(name = "appTaskExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("app-exec-");
        executor.setCorePoolSize(8);
        executor.setMaxPoolSize(64);
        executor.setQueueCapacity(1000);
        executor.setKeepAliveSeconds(60);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // Propagate MDC (correlation id, etc.) to async threads
        executor.setTaskDecorator(r -> {
            var contextMap = MDC.getCopyOfContextMap();
            return () -> {
                if (contextMap != null) MDC.setContextMap(contextMap);
                try { r.run(); } finally { MDC.clear(); }
            };
        });
        executor.initialize();
        log.info("Initialized ThreadPoolTaskExecutor: core={}, max={}, queue={} ",
                executor.getCorePoolSize(), executor.getMaxPoolSize(), executor.getThreadPoolExecutor().getQueue().remainingCapacity());
        return executor;
    }
}
