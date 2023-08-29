package com.library.libraryapp.service.book;

import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import com.library.libraryapp.domain.constants.IdResponse;
import com.library.libraryapp.domain.core.book.BookService;
import com.library.libraryapp.entity.elastic.book.ElasticBookDocument;
import com.library.libraryapp.exception.errors.NotFoundException;
import com.library.libraryapp.mapper.book.BookMapper;
import com.library.libraryapp.model.request.book.CreateBookRequest;
import com.library.libraryapp.model.response.book.BookResponse;
import com.library.libraryapp.model.response.book.BookSearchResponse;
import com.library.libraryapp.repository.book.BookRepository;
import com.library.libraryapp.repository.book.ElasticBookRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;

import static com.library.libraryapp.util.StringUtils.isNullOrEmpty;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {
    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ElasticBookRepository elasticBookRepository;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookResponse createBook(CreateBookRequest bookRequest) {
        if (bookRequest == null) throw new IllegalArgumentException();

        var bookEntity = bookMapper.toBookEntity(bookRequest);

        var book = bookRepository.save(bookEntity);
        elasticBookRepository.save(new ElasticBookDocument(book.getId(), book.getTitle(), book.getAuthor()));

        return bookMapper.toBookResponse(book);
    }

    @Override
    public List<BookResponse> getAllBooks() {
        return bookMapper.toBookResponse(bookRepository.findAll());
    }

    @Override
    public BookResponse getBookById(long id) {
        var book = bookRepository.findById(id).orElseThrow(NotFoundException::new);
        return bookMapper.toBookResponse(book);
    }

    @Override
    public BookResponse updateBook(long id, CreateBookRequest bookRequest) {
        if (bookRequest == null) throw new IllegalArgumentException();

        if (bookRepository.existsById(id)) {
            var book = bookMapper.toBookEntity(bookRequest);
            book.setId(id);

            elasticBookRepository.save(new ElasticBookDocument(book.getId(), book.getTitle(), book.getAuthor()));
            return bookMapper.toBookResponse(bookRepository.save(book));
        }

        throw new NotFoundException();
    }

    @Override
    public IdResponse<Long> deleteBook(long id) {
        bookRepository.deleteById(id);
        elasticBookRepository.deleteById(id);
        return new IdResponse<>(id);
    }

    @Override
    public List<BookSearchResponse> searchBooks(String keyword, Integer page, Integer limit) {
        if (isNullOrEmpty(keyword)) return Collections.emptyList();

        if (page == null) page = 0;
        if (limit == null) limit = 100;

        var query = QueryBuilders.multiMatch(builder -> builder
                .fields("title", "author")
                .operator(Operator.Or)
                .fuzziness("10")
                .query(keyword)
        );

        var nativeQuery = NativeQuery.builder()
                .withQuery(query)
                .withPageable(PageRequest.of(page, limit))
                .build();

        var searchResponse = elasticsearchTemplate.search(nativeQuery, ElasticBookDocument.class);
        return bookMapper.toBookResponse(searchResponse);
    }

    @Override
    public List<BookResponse> suggestBooks(List<String> genres, Integer limit) {
        if (limit == null) limit = 10;

        if (genres == null || genres.isEmpty())
            return bookMapper.toBookResponse(bookRepository.findAll(PageRequest.of(0, limit)).getContent());

        return bookMapper.toBookResponse(bookRepository.findByGenres(genres, limit));
    }
}

