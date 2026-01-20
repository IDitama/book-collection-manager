package com.book.collection.book_collection_manager.dto;

import com.book.collection.book_collection_manager.util.ISBN;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRequest {
    @NotBlank(message = "Title is required")
    @Size(max = 200)
    private String title;

    @NotBlank(message = "Author is required")
    @Size(max = 150)
    private String author;

    @ISBN
    private String isbn;

    private LocalDate publishedDate;

    @Size(max = 100)
    private String publisher;
}
