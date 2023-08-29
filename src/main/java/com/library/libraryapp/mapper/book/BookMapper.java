package com.library.libraryapp.mapper.book;

import com.library.libraryapp.entity.book.BookEntity;
import com.library.libraryapp.entity.elastic.book.ElasticBookDocument;
import com.library.libraryapp.model.request.book.CreateBookRequest;
import com.library.libraryapp.model.response.book.BookResponse;
import com.library.libraryapp.model.response.book.BookSearchResponse;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Component
public class BookMapper {

    public BookEntity toBookEntity(CreateBookRequest book) {
        if (book == null) return null;

        return BookEntity.builder()
                .title(book.title())
                .author(book.author())
                .genre(book.genre())
                .description(book.description())
                .isbn(book.isbn())
                .image(book.image())
                .publisher(book.publisher())
                .price(book.price())
                .currency(book.currency())
                .available(book.available())
                .build();
    }

    public BookResponse toBookResponse(BookEntity book) {
        if (book == null) return null;

        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                book.getDescription(),
                book.getIsbn(),
                book.getImage(),
                book.getPublished(),
                book.getPublisher(),
                book.getPrice(),
                book.getCurrency(),
                book.getAvailable()
        );
    }

    public List<BookResponse> toBookResponse(List<BookEntity> books) {
        if (books == null || books.isEmpty()) return Collections.emptyList();

        return books.stream()
                .map(book -> new BookResponse(
                        book.getId(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getGenre(),
                        book.getDescription(),
                        book.getIsbn(),
                        book.getImage(),
                        book.getPublished(),
                        book.getPublisher(),
                        book.getPrice(),
                        book.getCurrency(),
                        book.getAvailable()
                ))
                .toList();
    }

    public List<BookSearchResponse> toBookResponse(SearchHits<ElasticBookDocument> searchResult) {
        if (searchResult == null) return Collections.emptyList();

        return searchResult.getSearchHits()
                .stream()
                .flatMap(item -> Stream.of(item.getContent()))
                .map(book -> new BookSearchResponse(book.getId(), book.getTitle(), book.getAuthor()))
                .toList();
    }
}
