package com.library.libraryapp.controller.auth;

import com.library.libraryapp.domain.core.auth.AuthService;
import com.library.libraryapp.model.request.token.RefreshTokenRequest;
import com.library.libraryapp.model.request.login.LoginRequest;
import com.library.libraryapp.model.response.token.TokenResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthenticationController {
    private final AuthService authService;

    @PostMapping("/token")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(loginRequest));
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<TokenResponse> refreshTokens(@RequestBody @Valid RefreshTokenRequest tokenRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.refreshTokens(tokenRequest));
    }
}
