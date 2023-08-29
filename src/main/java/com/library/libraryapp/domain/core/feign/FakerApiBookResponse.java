package com.library.libraryapp.domain.core.feign;

import java.util.List;

public record FakerApiBookResponse(
        String status,
        Integer code,
        Integer total,
        List<FakerApiBookData> data
) {
    public record FakerApiBookData(
            Long id,
            String title,
            String author,
            String genre,
            String description,
            String isbn,
            String image,
            String published,
            String publisher
    ) {}
}
