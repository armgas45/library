package com.library.libraryapp.controller.admin;

import com.library.libraryapp.domain.core.book.BookService;
import com.library.libraryapp.domain.constants.IdResponse;
import com.library.libraryapp.domain.constants.UserRoles;
import com.library.libraryapp.model.request.book.CreateBookRequest;
import com.library.libraryapp.model.response.book.BookResponse;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/books")
@RolesAllowed(UserRoles.ROLE_ADMIN)
@AllArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping
    public ResponseEntity<BookResponse> addBook(@RequestBody @Valid CreateBookRequest bookRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.createBook(bookRequest));
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.getBookById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(@PathVariable long id,
                                                   @RequestBody @Valid CreateBookRequest bookRequest
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.updateBook(id, bookRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<IdResponse<Long>> deleteBook(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.deleteBook(id));
    }
}
