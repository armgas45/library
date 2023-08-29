package com.library.libraryapp.model.response.sales;

import com.library.libraryapp.model.response.book.BookResponse;

public record SalesReportResponse(
        Double montlyRevenue,
        Double averageOrderPrice,
        BookResponse topProduct
) {
}
