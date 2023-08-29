package com.library.libraryapp.model.request.user;

import jakarta.validation.constraints.NotBlank;

public record CreateAdminRequest(
        @NotBlank String name,
        String phone,
        @NotBlank String email,
        @NotBlank String password
) {
}
