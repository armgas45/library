package com.library.libraryapp.domain.core.user;

import com.library.libraryapp.model.request.user.CreateAdminRequest;
import com.library.libraryapp.model.request.user.CreateUserRequest;
import com.library.libraryapp.model.response.book.BookResponse;
import com.library.libraryapp.model.response.user.UserResponse;
import java.util.List;

public interface UserService {
    UserResponse signUp(CreateUserRequest user);
    UserResponse signUp(CreateAdminRequest adminUser);
    UserResponse getUserById(int id);
    List<BookResponse> suggestBooks(int userId, Integer limit);
}
