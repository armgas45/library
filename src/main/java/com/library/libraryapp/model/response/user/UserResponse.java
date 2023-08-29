package com.library.libraryapp.model.response.user;

import java.util.List;

public record UserResponse(
        Integer id,
        String name,
        String phone,
        String email,
        String address,
        String postalZip,
        String country,
        List<String> prefrences,
        String pan,
        String expdate,
        String cvv
) {
}
