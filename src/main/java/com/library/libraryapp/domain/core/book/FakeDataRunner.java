package com.library.libraryapp.domain.core.book;

import com.library.libraryapp.domain.core.feign.BookClient;
import com.library.libraryapp.entity.book.BookEntity;
import com.library.libraryapp.entity.elastic.book.ElasticBookDocument;
import com.library.libraryapp.exception.errors.NotFoundException;
import com.library.libraryapp.repository.book.BookRepository;
import com.library.libraryapp.repository.book.ElasticBookRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

// For mock data purposes only

@Component
@AllArgsConstructor
public class FakeDataRunner implements CommandLineRunner {
    private final BookClient bookClient;
    private final BookRepository bookRepository;
    private final ElasticBookRepository elasticBookRepository;

    // providing mock book data for project startup, only for testing purposes

    @Override
    public void run(String... args) throws Exception {
        var response = bookClient.getBookData(100, "en_US").getBody();
        List<BookEntity> books = new ArrayList<>();
        List<ElasticBookDocument> elasticBooks = new ArrayList<>();

        if (response == null) throw new NotFoundException();

        response.data()
                .forEach(book -> {
                    books.add(new BookEntity(
                            book.id(),
                            book.title(),
                            book.author(),
                            book.genre(),
                            book.description(),
                            book.isbn(),
                            book.image(),
                            book.published(),
                            book.publisher(),
                            9.99D,
                            "USD",
                            true));

                    elasticBooks.add(new ElasticBookDocument(book.id(), book.title(), book.author()));
                });

        bookRepository.saveAll(books);
        elasticBookRepository.saveAll(elasticBooks);
    }
}
