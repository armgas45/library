package com.library.libraryapp.model.request.purchase;

import jakarta.validation.constraints.NotNull;

public record BookBasket(
        @NotNull Long bookId,
        int quantity
) {
}
