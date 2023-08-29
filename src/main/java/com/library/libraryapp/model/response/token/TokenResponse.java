package com.library.libraryapp.model.response.token;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
