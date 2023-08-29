package com.library.libraryapp.service.auth;

import com.library.libraryapp.domain.core.token.TokenService;
import com.library.libraryapp.exception.errors.InvalidCredentialsException;
import com.library.libraryapp.model.request.login.LoginRequest;
import com.library.libraryapp.model.request.token.RefreshTokenRequest;
import com.library.libraryapp.util.jwt.JwtUtil;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import java.util.Collection;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class AuthServiceTests {

    @Spy
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void shouldLogin() {
        var authenticatedUser = new UsernamePasswordAuthenticationToken("username", "password");

        var accessToken = jwtUtil.getJwtToken("username", Collections.emptyList());
        var refreshToken = jwtUtil.getRefreshToken("username", Collections.emptyList());

        when(authenticationManager.authenticate(authenticatedUser)).thenReturn(authenticatedUser);

        when(tokenService.issueAccessToken(any(String.class), any(Collection.class)))
                .thenReturn(accessToken);

        when(tokenService.issueRefreshToken(any(String.class), any(Collection.class)))
                .thenReturn(refreshToken);

        var result = authService.login(new LoginRequest("username", "password"));

        assertThat(result).isNotNull();

        assertThat(result.accessToken()).isNotBlank();
        assertThat(result.refreshToken()).isNotBlank();

        assertThrows(IllegalArgumentException.class, () -> authService.login(null));
    }

    @Test
    void shouldFailLoginWhenProvidedInvalidCreadentials() {
        when(authenticationManager.authenticate(any())).thenThrow(InvalidCredentialsException.class);

        assertThrows(InvalidCredentialsException.class, () -> authService.login(new LoginRequest("username", "invalid-password")));
    }

    @Test
    void shouldGiveNewAccessAndRefreshTokens() {
        var accessToken = jwtUtil.getJwtToken("user", Collections.emptyList());
        var refreshToken = jwtUtil.getRefreshToken("user", Collections.emptyList());
        var request = new RefreshTokenRequest(refreshToken);

        when(tokenService.issueAccessTokenByRefreshToken(any(String.class))).thenReturn(accessToken);

        var tokens = authService.refreshTokens(request);

        assertThat(tokens.accessToken()).isEqualTo(accessToken);
        assertThat(tokens.refreshToken()).isEqualTo(refreshToken);
        assertThrows(IllegalArgumentException.class, () -> authService.refreshTokens(null));
    }

}