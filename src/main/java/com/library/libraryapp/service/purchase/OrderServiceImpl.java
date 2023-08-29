package com.library.libraryapp.service.purchase;

import com.library.libraryapp.domain.core.purchase.OrderService;
import com.library.libraryapp.entity.purchase.OrderEntity;
import com.library.libraryapp.exception.errors.NotFoundException;
import com.library.libraryapp.mapper.purchase.OrderMapper;
import com.library.libraryapp.model.request.purchase.PurchaseBookRequest;
import com.library.libraryapp.model.response.purchase.OrderResponse;
import com.library.libraryapp.repository.book.BookRepository;
import com.library.libraryapp.repository.order.OrderRepository;
import com.library.libraryapp.repository.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.concurrent.atomic.AtomicReference;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponse purchaseBook(PurchaseBookRequest purchaseBookRequest) {
        if (purchaseBookRequest == null) throw new IllegalArgumentException();

        var totalAmount = new AtomicReference<>(0D);

        var bookBasket = purchaseBookRequest.books()
                .stream()
                .map(item -> {
                    var book  = bookRepository.findById(item.bookId()).orElseThrow(NotFoundException::new);

                    totalAmount.set(totalAmount.get() + (book.getPrice() * item.quantity()));

                    return book;
                })
                .toList();

        var buyer = userRepository.findById(purchaseBookRequest.userId()).orElseThrow(NotFoundException::new);

        var order = OrderEntity.builder()
                .totalAmount(totalAmount.get())
                .currency("USD")
                .paymentAddress(buyer.getAddress())
                .paymentOption(purchaseBookRequest.paymentDetails().paymentOption())
                .promocode(purchaseBookRequest.paymentDetails().promocode())
                .user(buyer)
                .books(bookBasket)
                .build();

        return orderMapper.toOrderResponse(orderRepository.save(order));
    }
}
