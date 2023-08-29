package com.library.libraryapp.model.response.book;

public record BookSearchResponse(
        Long id,
        String title,
        String author
) {
}
