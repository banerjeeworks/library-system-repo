package com.example.cqs.controller;

import com.example.cqs.domain.Book;
import com.example.cqs.dto.BookDto;
import com.example.cqs.mapper.BookMapper;
import com.example.cqs.service.BookCommandService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookCommandController {
    private final BookCommandService commandService;

    public BookCommandController(BookCommandService commandService) {
        this.commandService = commandService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDto> create(@Valid @RequestBody BookDto dto) {
        Book created = commandService.create(BookMapper.toEntity(dto));
        return ResponseEntity.ok(BookMapper.toDto(created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDto> update(@PathVariable Long id, @Valid @RequestBody BookDto dto) {
        Book updated = commandService.update(id, BookMapper.toEntity(dto));
        return ResponseEntity.ok(BookMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        commandService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
