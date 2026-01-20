package com.book.collection.book_collection_manager.messaging;

import com.book.collection.book_collection_manager.model.Book;
import com.book.collection.book_collection_manager.repository.BookRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class BookMessageListener {
    private final BookRepository bookRepository;

    public BookMessageListener(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional
    @RabbitListener(queues = "${rabbitmq.queue}")
    public void handleInsert(BookMessage message) {
        // idempotency: skip if ISBN already exists
        if (bookRepository.existsByIsbn(message.getIsbn())) return;
        Book book = new Book();
        book.setTitle(message.getTitle());
        book.setAuthor(message.getAuthor());
        book.setIsbn(message.getIsbn());
        book.setPublishedDate(message.getPublishedDate());
        book.setPublisher(message.getPublisher());
        bookRepository.save(book);
    }

}
