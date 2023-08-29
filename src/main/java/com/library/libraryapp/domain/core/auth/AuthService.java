package com.library.libraryapp.domain.core.auth;

import com.library.libraryapp.model.request.token.RefreshTokenRequest;
import com.library.libraryapp.model.request.login.LoginRequest;
import com.library.libraryapp.model.response.token.TokenResponse;

public interface AuthService {
    TokenResponse login(LoginRequest loginRequest);
    TokenResponse refreshTokens(RefreshTokenRequest refreshToken);
}
