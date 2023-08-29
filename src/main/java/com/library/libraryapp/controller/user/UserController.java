package com.library.libraryapp.controller.user;

import com.library.libraryapp.domain.constants.UserRoles;
import com.library.libraryapp.domain.core.book.BookService;
import com.library.libraryapp.domain.core.purchase.OrderService;
import com.library.libraryapp.domain.core.user.UserService;
import com.library.libraryapp.model.request.purchase.PurchaseBookRequest;
import com.library.libraryapp.model.request.user.CreateUserRequest;
import com.library.libraryapp.model.response.book.BookResponse;
import com.library.libraryapp.model.response.book.BookSearchResponse;
import com.library.libraryapp.model.response.purchase.OrderResponse;
import com.library.libraryapp.model.response.user.UserResponse;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {
    private final BookService bookService;
    private final UserService userService;
    private final OrderService orderService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signUp(@RequestBody @Valid CreateUserRequest createUserRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.signUp(createUserRequest));
    }

    @RolesAllowed(UserRoles.ROLE_USER)
    @GetMapping("/{id}/suggest")
    public ResponseEntity<List<BookResponse>> suggestBooks(@PathVariable int id,
                                                           @RequestParam(required = false) Integer limit
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.suggestBooks(id, limit));
    }

    @RolesAllowed(UserRoles.ROLE_USER)
    @GetMapping("/search")
    public ResponseEntity<List<BookSearchResponse>> searchForBook(@RequestParam String keyword,
                                                                  @RequestParam(required = false) Integer page,
                                                                  @RequestParam(required = false) Integer limit
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.searchBooks(keyword, page, limit));
    }

    @RolesAllowed(UserRoles.ROLE_USER)
    @PostMapping("/purchase")
    public ResponseEntity<OrderResponse> purchaseBook(@RequestBody @Valid PurchaseBookRequest purchaseRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.purchaseBook(purchaseRequest));
    }
}
