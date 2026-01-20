package com.book.collection.book_collection_manager.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookMessage {
    private String title;
    private String author;
    private String isbn;
    private LocalDate publishedDate;
    private String publisher;

}
