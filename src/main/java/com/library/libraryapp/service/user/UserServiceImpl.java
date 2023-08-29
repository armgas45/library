package com.library.libraryapp.service.user;

import com.library.libraryapp.domain.constants.UserRoles;
import com.library.libraryapp.domain.core.book.BookService;
import com.library.libraryapp.domain.core.user.UserService;
import com.library.libraryapp.exception.errors.NotFoundException;
import com.library.libraryapp.mapper.user.UserMapper;
import com.library.libraryapp.model.request.user.CreateAdminRequest;
import com.library.libraryapp.model.request.user.CreateUserRequest;
import com.library.libraryapp.model.response.book.BookResponse;
import com.library.libraryapp.model.response.user.UserResponse;
import com.library.libraryapp.repository.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final BookService bookService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserResponse signUp(CreateUserRequest user) {
        if (user == null) throw new IllegalArgumentException();

        var userEntity = userMapper.toUserEntity(user);

        // encoding the raw password
        userEntity.setPassword(passwordEncoder.encode(user.password()));
        userEntity.setRole("ROLE_" + UserRoles.ROLE_USER);

        return userMapper.toUserResponse(userRepository.save(userEntity));
    }

    @Override
    public UserResponse signUp(CreateAdminRequest adminUser) {
        if (adminUser == null) throw new IllegalArgumentException();

        var userEntity = userMapper.toUserEntity(adminUser);

        // encoding the raw password
        userEntity.setPassword(passwordEncoder.encode(adminUser.password()));
        userEntity.setRole("ROLE_" + UserRoles.ROLE_ADMIN);

        return userMapper.toUserResponse(userRepository.save(userEntity));
    }

    @Override
    public UserResponse getUserById(int id) {
        var user = userRepository.findById(id).orElseThrow(NotFoundException::new);
        return userMapper.toUserResponse(user);
    }

    @Override
    public List<BookResponse> suggestBooks(int userId, Integer limit) {
        var userPrefrredGenres = userRepository.findById(userId)
                .orElseThrow(NotFoundException::new)
                .getPreferedGenres();

        return bookService.suggestBooks(userPrefrredGenres, limit);
    }
}
