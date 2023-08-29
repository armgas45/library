package com.library.libraryapp.service.sales;

import com.library.libraryapp.domain.core.sales.SalesReportService;
import com.library.libraryapp.mapper.book.BookMapper;
import com.library.libraryapp.model.response.sales.SalesReportResponse;
import com.library.libraryapp.repository.book.BookRepository;
import com.library.libraryapp.repository.order.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SalesReportServiceImpl implements SalesReportService {
    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public SalesReportResponse getSalesReport() {
        var montlyRevenue = orderRepository.getMontlyRevenue().orElse(0D);
        var averageOrderPrice = orderRepository.getAverageOrderPrice();
        var montlyTopSoldProduct = bookRepository.getMontlyTopProduct();

        return new SalesReportResponse(montlyRevenue, averageOrderPrice, bookMapper.toBookResponse(montlyTopSoldProduct));
    }
}
