package com.library.libraryapp.service.auth;

import com.library.libraryapp.domain.core.auth.AuthService;
import com.library.libraryapp.domain.core.token.TokenService;
import com.library.libraryapp.exception.errors.InvalidCredentialsException;
import com.library.libraryapp.model.request.token.RefreshTokenRequest;
import com.library.libraryapp.model.request.login.LoginRequest;
import com.library.libraryapp.model.response.token.TokenResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Override
    public TokenResponse login(LoginRequest loginRequest) {
        if (loginRequest == null) throw new IllegalArgumentException();

        Authentication authenticatedUser;

        try {
            authenticatedUser = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
            );
        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException();
        }

        return new TokenResponse(
                tokenService.issueAccessToken(loginRequest.username(), authenticatedUser.getAuthorities()),
                tokenService.issueRefreshToken(loginRequest.username(), authenticatedUser.getAuthorities())
        );
    }

    @Override
    public TokenResponse refreshTokens(RefreshTokenRequest refreshTokenRequest) {
        if (refreshTokenRequest == null) throw new IllegalArgumentException();

        return new TokenResponse(
                tokenService.issueAccessTokenByRefreshToken(refreshTokenRequest.refreshToken()),
                refreshTokenRequest.refreshToken()
        );
    }
}
