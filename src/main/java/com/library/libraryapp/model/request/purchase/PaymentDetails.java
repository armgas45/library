package com.library.libraryapp.model.request.purchase;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.library.libraryapp.domain.constants.PaymentOption;

public record PaymentDetails(
        @JsonProperty("payment-option")
        PaymentOption paymentOption,
        String promocode
) {
}
