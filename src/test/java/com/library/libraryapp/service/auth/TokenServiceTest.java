package com.library.libraryapp.service.auth;

import com.library.libraryapp.domain.constants.UserRoles;
import com.library.libraryapp.entity.token.RefreshTokenEntity;
import com.library.libraryapp.entity.token.RevokedAccessTokenEntity;
import com.library.libraryapp.repository.tokens.RefreshTokenRepository;
import com.library.libraryapp.repository.tokens.RevokedAccessTokenRepository;
import com.library.libraryapp.service.auth.token.TokenServiceImpl;
import com.library.libraryapp.util.jwt.JwtUtil;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class TokenServiceTest {

    @Mock
    private RevokedAccessTokenRepository revokedAccessTokenRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private TokenServiceImpl tokenService;

    private String username;
    private List<GrantedAuthority> roles;

    @BeforeEach
    void setUp() {
        username = "username";
        roles = List.of(new SimpleGrantedAuthority("ROLE_" + UserRoles.ROLE_USER));
    }

    @Test
    void shouldIssueAccessToken() {
        when(jwtUtil.getJwtToken(username, roles)).thenReturn("access-token");
        var result = tokenService.issueAccessToken(username, roles);

        assertThat(result).isNotBlank();
        assertThrows(IllegalArgumentException.class, () -> tokenService.issueAccessToken("", roles));
        assertThrows(IllegalArgumentException.class, () -> tokenService.issueAccessToken(username, null));
    }

    @Test
    void shouldIssueRefreshToken() {
        when(jwtUtil.getRefreshToken(username, roles)).thenReturn("refresh-token");
        when(jwtUtil.extractTokenId(any())).thenReturn(UUID.randomUUID());
        when(jwtUtil.extractExpiration(any())).thenReturn(1L);
        when(refreshTokenRepository.save(any())).thenReturn(new RefreshTokenEntity());

        var result = tokenService.issueRefreshToken(username, roles);

        assertThat(result).isNotBlank();

        verify(refreshTokenRepository, atMostOnce()).save(any());

        assertThrows(IllegalArgumentException.class, () -> tokenService.issueRefreshToken("", roles));
        assertThrows(IllegalArgumentException.class, () -> tokenService.issueRefreshToken(username, null));
    }

    @Test
    void shouldIssueAccessTokenByRefreshToken() {
        when(jwtUtil.isRefreshToken(any())).thenReturn(true);
        when(jwtUtil.validateToken(any())).thenReturn(true);
        when(jwtUtil.extractTokenId(any())).thenReturn(UUID.randomUUID());

        when(refreshTokenRepository.existsById(any())).thenReturn(true);

        when(jwtUtil.extractUsername(any())).thenReturn(username);
        when(jwtUtil.extractRoles(any())).thenReturn(List.of("ROLE_USER"));
        when(jwtUtil.getJwtToken(any(), any())).thenReturn("access-token");

        var result = tokenService.issueAccessTokenByRefreshToken("refresh-token");

        assertThat(result).isNotBlank();
        assertThrows(IllegalArgumentException.class, () -> tokenService.issueAccessTokenByRefreshToken(""));
    }

    @Test
    void shouldFailValidatingWhenInvalidRefreshTokenProvided() {
        when(jwtUtil.validateToken(any())).thenReturn(false);

        assertThrows(JwtException.class, () -> tokenService.issueAccessTokenByRefreshToken("refresh-token"));
    }

    @Test
    void shouldFailIfUnknownRefreshTokenProvided() {
        when(refreshTokenRepository.existsById(any())).thenReturn(false);

        // todo unkown exception
        assertThrows(RuntimeException.class, () -> tokenService.issueAccessTokenByRefreshToken("refresh-token"));
    }

    @Test
    void shouldValidateAccessToken() {
        when(jwtUtil.validateToken(any())).thenReturn(true);
        when(revokedAccessTokenRepository.existsById(any())).thenReturn(false);

        var result = tokenService.validateAccessToken("access-token");

        assertThat(result).isTrue();
        assertThat(tokenService.validateAccessToken("")).isFalse();
    }

    @Test
    void shouldFailWhenAccessTokenIsInvalid() {
        when(jwtUtil.validateToken(any())).thenReturn(false);

        var result = tokenService.validateAccessToken("access-token");

        assertThat(result).isFalse();
    }

    @Test
    void shouldFailWhenUserIsLoggedOut() {
        when(jwtUtil.validateToken(any())).thenReturn(true);
        when(revokedAccessTokenRepository.existsById(any())).thenReturn(true);

        var result = tokenService.validateAccessToken("access-token");

        assertThat(result).isFalse();
    }


    @Test
    void shouldRevokeAccessToken() {
        when(revokedAccessTokenRepository.save(any())).thenReturn(new RevokedAccessTokenEntity());

        tokenService.revokeAccessToken("access-token");

        verify(revokedAccessTokenRepository, atLeastOnce()).save(any());

        assertThrows(IllegalArgumentException.class, () -> tokenService.revokeAccessToken(""));
    }

    @Test
    void shouldRevokeRefreshToken() {

        doNothing().when(refreshTokenRepository).deleteById(any());

        tokenService.revokeRefreshToken("refresh-token");

        verify(refreshTokenRepository, atLeastOnce()).deleteById(any());

        assertThrows(IllegalArgumentException.class, () -> tokenService.revokeRefreshToken(""));
    }

}