package com.example.cqs.controller;

import com.example.cqs.dto.BookDto;
import com.example.cqs.mapper.BookMapper;
import com.example.cqs.service.BookQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
public class BookQueryController {
    private final BookQueryService queryService;
    private final Executor executor;

    public BookQueryController(BookQueryService queryService, @org.springframework.beans.factory.annotation.Qualifier("appTaskExecutor") Executor executor) {
        this.queryService = queryService;
        this.executor = executor;
    }

    @GetMapping
    public ResponseEntity<List<BookDto>> getAll() {
        var books = queryService.getAll().stream().map(BookMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getById(@PathVariable Long id) {
        return queryService.getById(id)
                .map(BookMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookDto> getByIsbn(@PathVariable String isbn) {
        return queryService.getByIsbn(isbn)
                .map(BookMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/large")
    public ResponseEntity<List<String>> large(@RequestParam(defaultValue = "1000") int size,
                                              @RequestParam(defaultValue = "8") int payloadKb) {
        return ResponseEntity.ok(queryService.getLargePayload(size, payloadKb));
    }

    // High-concurrency batch fetch: concurrently fetch multiple IDs and aggregate results
    @GetMapping("/ids")
    public ResponseEntity<List<BookDto>> getByIds(@RequestParam List<Long> ids) {
        List<CompletableFuture<Optional<com.example.cqs.domain.Book>>> futures = ids.stream()
                .map(queryService::getByIdAsync)
                .collect(Collectors.toList());

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        List<BookDto> result = futures.stream()
                .map(CompletableFuture::join)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(BookMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }
}
