package com.book.collection.book_collection_manager.controller;

import com.book.collection.book_collection_manager.dto.BookBatchRequest;
import com.book.collection.book_collection_manager.dto.BookRequest;
import com.book.collection.book_collection_manager.dto.BookResponse;
import com.book.collection.book_collection_manager.messaging.BookMessage;
import com.book.collection.book_collection_manager.messaging.BookMessageProducer;
import com.book.collection.book_collection_manager.model.Book;
import com.book.collection.book_collection_manager.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;
    private final BookMessageProducer producer;

    public BookController(BookService bookService, BookMessageProducer producer) {
        this.bookService = bookService;
        this.producer = producer;
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> getAll() {
        List<BookResponse> responses = bookService.findAll().stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getById(@PathVariable Long id) {
        Book book = bookService.findById(id);
        return ResponseEntity.ok(toResponse(book));
    }

    // Single insert via RabbitMQ
    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody Object payload) {
        // Accept either single BookRequest or BookBatchRequest
        if (payload instanceof BookRequest req) {
            producer.send(toMessage(req));
        } else if (payload instanceof BookBatchRequest batch) {
            batch.getBooks().forEach(req -> producer.send(toMessage(req)));
        } else {
            return ResponseEntity.badRequest().build();
        }
        // 202 Accepted since processing is async
        return ResponseEntity.accepted().location(URI.create("/api/books")).build();
    }

    // Alternative: explicit endpoints (uncomment if you prefer strict typing)
  /*
  @PostMapping("/batch")
  public ResponseEntity<Void> createBatch(@Valid @RequestBody BookBatchRequest batch) {
    batch.getBooks().forEach(req -> producer.send(toMessage(req)));
    return ResponseEntity.accepted().build();
  }

  @PostMapping("/single")
  public ResponseEntity<Void> createSingle(@Valid @RequestBody BookRequest req) {
    producer.send(toMessage(req));
    return ResponseEntity.accepted().build();
  }
  */

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> update(@PathVariable Long id, @Valid @RequestBody BookRequest req) {
        Book updated = bookService.update(id, req);
        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private BookResponse toResponse(Book b) {
        return new BookResponse(b.getId(), b.getTitle(), b.getAuthor(), b.getIsbn(), b.getPublishedDate(), b.getPublisher());
    }

    private BookMessage toMessage(BookRequest req) {
        return new BookMessage(req.getTitle(), req.getAuthor(), req.getIsbn(), req.getPublishedDate(), req.getPublisher());
    }

}
