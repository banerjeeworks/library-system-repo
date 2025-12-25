package com.example.cqs.events;

import com.example.cqs.domain.Book;
import org.springframework.context.ApplicationEvent;

public abstract class BookEvents extends ApplicationEvent {
    private final Book book;

    public BookEvents(Object source, Book book) {
        super(source);
        this.book = book;
    }

    public Book getBook() {
        return book;
    }

    public static class Created extends BookEvents {
        public Created(Object source, Book book) { super(source, book); }
    }
    public static class Updated extends BookEvents {
        public Updated(Object source, Book book) { super(source, book); }
    }
    public static class Deleted extends BookEvents {
        public Deleted(Object source, Book book) { super(source, book); }
    }
}
