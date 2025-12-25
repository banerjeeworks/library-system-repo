package com.example.cqs.mapper;

import com.example.cqs.domain.Book;
import com.example.cqs.dto.BookDto;

public class BookMapper {
    public static BookDto toDto(Book book) {
        if (book == null) return null;
        return new BookDto(book.getId(), book.getTitle(), book.getAuthor(), book.getIsbn());
    }

    public static Book toEntity(BookDto dto) {
        if (dto == null) return null;
        Book b = new Book();
        b.setId(dto.getId());
        b.setTitle(dto.getTitle());
        b.setAuthor(dto.getAuthor());
        b.setIsbn(dto.getIsbn());
        return b;
    }
}
