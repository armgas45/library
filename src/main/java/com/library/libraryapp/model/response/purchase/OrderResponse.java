package com.library.libraryapp.model.response.purchase;

import com.library.libraryapp.model.response.book.BookResponse;
import java.util.List;

public record OrderResponse(
        long orderId,
        double total,
        List<BookResponse> basket
) {
}
