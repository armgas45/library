package com.library.libraryapp.model.response.book;

public record BookResponse(
        long id,
        String title,
        String author,
        String genre,
        String description,
        String isbn,
        String image,
        String published,
        String publisher,
        Double price,
        String currency,
        Boolean available
) {
}
