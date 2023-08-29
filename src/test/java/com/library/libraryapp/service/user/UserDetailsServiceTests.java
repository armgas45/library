package com.library.libraryapp.service.user;

import com.library.libraryapp.domain.constants.UserRoles;
import com.library.libraryapp.entity.user.UserEntity;
import com.library.libraryapp.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserDetailsServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ApplicationUserDetailsService userDetailsService;

    @Test
    void shouldLoadUserByUsername() {
        var username = "username";
        var user = new UserEntity(1, username, null, "user@gmail.com", null, null, null, "password", null, null, null, UserRoles.ROLE_USER, null, null);

        when(userRepository.findByName(username)).thenReturn(Optional.of(user));
        var result = userDetailsService.loadUserByUsername(username);

        assertThat(result.getUsername()).isEqualTo(username);
        assertThat(result.getAuthorities()).isNotEmpty();

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("invalid-username"));
    }
}
