package com.library.libraryapp.service.user;

import com.library.libraryapp.domain.constants.UserRoles;
import com.library.libraryapp.entity.user.UserEntity;
import com.library.libraryapp.exception.errors.NotFoundException;
import com.library.libraryapp.mapper.user.UserMapper;
import com.library.libraryapp.model.request.user.CreateAdminRequest;
import com.library.libraryapp.model.request.user.CreateUserRequest;
import com.library.libraryapp.model.response.user.UserResponse;
import com.library.libraryapp.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Spy
    private PasswordEncoder passwordEncoder;

    @Spy
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldSignUpRegularUser() {
        var user = mockUser();
        var userResponse = mockUserResponse();

        when(userMapper.toUserEntity(any(CreateUserRequest.class))).thenReturn(user);
        when(userMapper.toUserResponse(any(UserEntity.class))).thenReturn(userResponse);
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        var result = userService.signUp(mockCreateUserRequest());

        assertThat(result).isEqualTo(userResponse);
        assertThat(user.getRole()).isEqualTo("ROLE_" + UserRoles.ROLE_USER);

        verify(userRepository, atMostOnce()).save(user);

        assertThrows(IllegalArgumentException.class, () -> userService.signUp((CreateUserRequest) null));
    }

    @Test
    void shouldSignUpAdminUser() {
        var user = mockUser();
        var userResponse = mockUserResponse();

        when(userMapper.toUserEntity(any(CreateAdminRequest.class))).thenReturn(user);
        when(userMapper.toUserResponse(any(UserEntity.class))).thenReturn(userResponse);
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        var result = userService.signUp(mockCreateAdminUserRequest());

        assertThat(result).isEqualTo(userResponse);
        assertThat(user.getRole()).isEqualTo("ROLE_" + UserRoles.ROLE_ADMIN);

        verify(userRepository, atMostOnce()).save(user);

        assertThrows(IllegalArgumentException.class, () -> userService.signUp((CreateUserRequest) null));
    }

    @Test
    void shouldGetUserById() {
        var user = mockUser();
        var userResponse = mockUserResponse();

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.findById(-1)).thenThrow(NotFoundException.class);
        when(userMapper.toUserResponse(any(UserEntity.class))).thenReturn(userResponse);

        var result = userService.getUserById(1);

        assertThat(result).isEqualTo(userResponse);

        verify(userRepository, atMostOnce()).save(user);

        assertThrows(NotFoundException.class, () -> userRepository.findById(-1));
    }

    private CreateUserRequest mockCreateUserRequest() {
        return new CreateUserRequest(
                "user",
                "111111111",
                "user@gmail.com",
                "address 1",
                "zip",
                "country",
                "pass",
                Collections.emptyList(),
                "pan",
                "01/24",
                "123"
        );
    }

    private CreateAdminRequest mockCreateAdminUserRequest() {
        return new CreateAdminRequest(
                "admin-user",
                "11111",
                "admin@gmail.com",
                "123"
        );
    }

    private UserEntity mockUser() {
        return new UserEntity(
                1,
                "admin-user",
                "111111111",
                "admin@gmail.com",
                "address 1",
                "zip",
                "country",
                "password",
                "pan",
                "01/24",
                "123",
                null,
                null,
                null
        );
    }

    private UserResponse mockUserResponse() {
        return new UserResponse(
                1,
                "user",
                "111111111",
                "user@gmail.com",
                "address 1",
                "zip",
                "country",
                Collections.emptyList(),
                "pan",
                "01/24",
                "123"
        );
    }
}
