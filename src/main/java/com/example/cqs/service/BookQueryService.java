package com.example.cqs.service;

import com.example.cqs.dao.BookDao;
import com.example.cqs.domain.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class BookQueryService {
    private static final Logger log = LoggerFactory.getLogger(BookQueryService.class);
    private final BookDao dao;
    private final Random random = new Random();

    public BookQueryService(BookDao dao) {
        this.dao = dao;
    }

    @Cacheable("book-by-id")
    public Optional<Book> getById(Long id) {
        artificialDelay();
        return dao.findById(id);
    }

    @Cacheable("book-by-isbn")
    public Optional<Book> getByIsbn(String isbn) {
        artificialDelay();
        return dao.findByIsbn(isbn);
    }

    @Cacheable("book-all")
    public List<Book> getAll() {
        artificialDelay();
        return dao.findAll();
    }

    // Simulate heavy payload for performance testing
    @Cacheable("book-large-payload")
    public List<String> getLargePayload(int size, int payloadKb) {
        artificialDelay();
        List<String> payload = new ArrayList<>(size);
        String block = "x".repeat(Math.max(1, payloadKb) * 1024);
        for (int i = 0; i < size; i++) {
            payload.add(block);
        }
        return payload;
    }

    private void artificialDelay() {
        try {
            // 50-150ms delay to simulate remote call
            TimeUnit.MILLISECONDS.sleep(50 + random.nextInt(100));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
