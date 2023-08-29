package com.library.libraryapp.domain.core.book;

import com.library.libraryapp.domain.constants.IdResponse;
import com.library.libraryapp.model.request.book.CreateBookRequest;
import com.library.libraryapp.model.response.book.BookResponse;
import com.library.libraryapp.model.response.book.BookSearchResponse;
import java.util.List;

public interface BookService {
    BookResponse createBook(CreateBookRequest bookRequest);
    List<BookResponse> getAllBooks();
    BookResponse getBookById(long id);
    BookResponse updateBook(long id, CreateBookRequest bookRequest);
    IdResponse<Long> deleteBook(long id);
    List<BookSearchResponse> searchBooks(String keyword, Integer page, Integer limit);
    List<BookResponse> suggestBooks(List<String> genres, Integer limit);
}
