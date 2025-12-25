package com.example.cqs.service;

import com.example.cqs.dao.BookDao;
import com.example.cqs.domain.Book;
import com.example.cqs.events.BookEvents;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookCommandService {
    private final BookDao dao;
    private final ApplicationEventPublisher publisher;

    public BookCommandService(BookDao dao, ApplicationEventPublisher publisher) {
        this.dao = dao;
        this.publisher = publisher;
    }

    @Transactional
    @CacheEvict(value = {"book-all", "book-by-id", "book-by-isbn", "book-large-payload"}, allEntries = true)
    public Book create(Book book) {
        Book saved = dao.save(book);
        publisher.publishEvent(new BookEvents.Created(this, saved));
        return saved;
    }

    @Transactional
    @CacheEvict(value = {"book-all", "book-by-id", "book-by-isbn", "book-large-payload"}, allEntries = true)
    public Book update(Long id, Book update) {
        Book existing = dao.findById(id).orElseThrow(() -> new IllegalArgumentException("Book not found: " + id));
        existing.setTitle(update.getTitle());
        existing.setAuthor(update.getAuthor());
        existing.setIsbn(update.getIsbn());
        Book saved = dao.save(existing);
        publisher.publishEvent(new BookEvents.Updated(this, saved));
        return saved;
    }

    @Transactional
    @CacheEvict(value = {"book-all", "book-by-id", "book-by-isbn", "book-large-payload"}, allEntries = true)
    public void delete(Long id) {
        Book existing = dao.findById(id).orElseThrow(() -> new IllegalArgumentException("Book not found: " + id));
        dao.deleteById(id);
        publisher.publishEvent(new BookEvents.Deleted(this, existing));
    }
}
