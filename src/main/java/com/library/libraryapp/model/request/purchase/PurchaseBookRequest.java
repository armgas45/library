package com.library.libraryapp.model.request.purchase;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record PurchaseBookRequest(
        @NotNull Integer userId,

        @JsonProperty("book-list")
        @NotNull List<BookBasket> books,

        @JsonProperty("payment-details")
        @NotNull @Valid PaymentDetails paymentDetails
) {}