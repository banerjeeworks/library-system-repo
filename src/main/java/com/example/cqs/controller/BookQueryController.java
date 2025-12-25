package com.example.cqs.controller;

import com.example.cqs.dto.BookDto;
import com.example.cqs.mapper.BookMapper;
import com.example.cqs.service.BookQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
public class BookQueryController {
    private final BookQueryService queryService;

    public BookQueryController(BookQueryService queryService) {
        this.queryService = queryService;
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
}
