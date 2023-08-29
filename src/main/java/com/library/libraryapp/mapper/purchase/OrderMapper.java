package com.library.libraryapp.mapper.purchase;

import com.library.libraryapp.entity.purchase.OrderEntity;
import com.library.libraryapp.model.response.book.BookResponse;
import com.library.libraryapp.model.response.purchase.OrderResponse;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderResponse toOrderResponse(OrderEntity order) {
        if (order == null) return null;

        var books = order.getBooks()
                .stream()
                .map(book -> new BookResponse(
                        book.getId(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getGenre(),
                        book.getDescription(),
                        book.getIsbn(),
                        book.getImage(),
                        book.getPublished(),
                        book.getPublisher(),
                        book.getPrice(),
                        book.getCurrency(),
                        book.getAvailable()
                ))
                .toList();

        return new OrderResponse(order.getId(), order.getTotalAmount(), books);
    }
}
