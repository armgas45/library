package com.library.libraryapp.model.request.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateBookRequest(
        @NotBlank String title,
        @NotBlank String author,
        @NotBlank String genre,
        @NotBlank String description,
        @NotBlank String isbn,
        String image,
        String published,
        @NotBlank String publisher,
        @NotNull Double price,
        @NotBlank String currency,
        Boolean available
) {
}