package com.library.libraryapp.domain.core.purchase;

import com.library.libraryapp.model.request.purchase.PurchaseBookRequest;
import com.library.libraryapp.model.response.purchase.OrderResponse;

public interface OrderService {
    OrderResponse purchaseBook(PurchaseBookRequest purchaseBookRequest);
}
