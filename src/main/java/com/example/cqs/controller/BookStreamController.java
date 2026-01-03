package com.example.cqs.controller;

import com.example.cqs.stream.SsePublisher;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/books")
public class BookStreamController {
    private final SsePublisher publisher;

    public BookStreamController(SsePublisher publisher) {
        this.publisher = publisher;
    }

    // Subscribe to domain events as Server-Sent Events (SSE)
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream() {
        // 30 minutes
        return publisher.subscribe(30 * 60 * 1000L);
    }
}
