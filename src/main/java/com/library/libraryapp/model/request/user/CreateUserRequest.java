package com.library.libraryapp.model.request.user;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record CreateUserRequest(
        @NotBlank String name,
        String phone,
        @NotBlank String email,
        String address,
        String postalZip,
        String country,
        @NotBlank String password,
        List<String> preferences,
        String pan,
        String expdate,
        String cvv
) {
}
