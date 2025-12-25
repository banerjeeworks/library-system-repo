package com.example.cqs.dao;

import com.example.cqs.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookDao {
    Book save(Book book);
    Optional<Book> findById(Long id);
    Optional<Book> findByIsbn(String isbn);
    List<Book> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
    long count();
}
