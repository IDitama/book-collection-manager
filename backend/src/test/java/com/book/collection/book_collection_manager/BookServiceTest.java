package com.book.collection.book_collection_manager;

import com.book.collection.book_collection_manager.dto.BookRequest;
import com.book.collection.book_collection_manager.model.Book;
import com.book.collection.book_collection_manager.repository.BookRepository;
import com.book.collection.book_collection_manager.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class BookServiceTest {
    private BookRepository repo;
    private BookService service;

    @BeforeEach
    void setup() {
        repo = Mockito.mock(BookRepository.class);
        service = new BookService(repo);
    }

    @Test
    void findById_found() {
        Book b = new Book();
        b.setTitle("Title");
        b.setAuthor("Author");
        b.setIsbn("9780306406157");
        b.setPublishedDate(LocalDate.now());
        b.setPublisher("Pub");
        b.setId(1L);
        when(repo.findById(1L)).thenReturn(Optional.of(b));

        Book result = service.findById(1L);
        assertEquals("Title", result.getTitle());
    }

    @Test
    void findById_notFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.findById(99L));
    }

    @Test
    void update_success() {
        Book existing = new Book();
        existing.setTitle("Old");
        existing.setAuthor("A");
        existing.setIsbn("9780306406157");
        existing.setPublishedDate(null);
        existing.setPublisher(null);
        existing.setId(2L);
        when(repo.findById(2L)).thenReturn(Optional.of(existing));
        when(repo.save(any(Book.class))).thenAnswer(inv -> inv.getArgument(0));

        BookRequest req = new BookRequest();
        req.setTitle("New");
        req.setAuthor("B");
        req.setIsbn("9780306406157");
        req.setPublishedDate(LocalDate.of(2020,1,1));
        req.setPublisher("X");

        Book updated = service.update(2L, req);
        assertEquals("New", updated.getTitle());
        assertEquals("B", updated.getAuthor());
    }

    @Test
    void delete_success() {
        when(repo.existsById(3L)).thenReturn(true);
        service.delete(3L);
        verify(repo, times(1)).deleteById(3L);
    }

    @Test
    void delete_notFound() {
        when(repo.existsById(4L)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> service.delete(4L));
    }
}
