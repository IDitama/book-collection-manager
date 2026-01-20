package com.book.collection.book_collection_manager.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookBatchRequest {
    @NotEmpty(message = "Books list cannot be empty")
    @Valid
    private List<BookRequest> books;
}
