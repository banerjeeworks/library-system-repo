package com.example.cqs.events;

import com.example.cqs.dto.BookDto;
import com.example.cqs.mapper.BookMapper;
import com.example.cqs.stream.SsePublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class BookEventListener {
    private static final Logger log = LoggerFactory.getLogger(BookEventListener.class);
    private final SsePublisher ssePublisher;

    public BookEventListener(SsePublisher ssePublisher) {
        this.ssePublisher = ssePublisher;
    }

    @Async
    @EventListener
    public void onCreated(BookEvents.Created evt) {
        log.info("[ASYNC] Book created: id={}, title={}", evt.getBook().getId(), evt.getBook().getTitle());
        ssePublisher.broadcast(new DomainEventPayload("BOOK_CREATED", BookMapper.toDto(evt.getBook())));
    }

    @Async
    @EventListener
    public void onUpdated(BookEvents.Updated evt) {
        log.info("[ASYNC] Book updated: id={}, title={}", evt.getBook().getId(), evt.getBook().getTitle());
        ssePublisher.broadcast(new DomainEventPayload("BOOK_UPDATED", BookMapper.toDto(evt.getBook())));
    }

    @Async
    @EventListener
    public void onDeleted(BookEvents.Deleted evt) {
        log.info("[ASYNC] Book deleted: id={}, title={}", evt.getBook().getId(), evt.getBook().getTitle());
        ssePublisher.broadcast(new DomainEventPayload("BOOK_DELETED", BookMapper.toDto(evt.getBook())));
    }

    public record DomainEventPayload(String type, BookDto book) {}
}
