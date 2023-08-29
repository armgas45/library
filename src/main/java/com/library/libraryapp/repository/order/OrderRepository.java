package com.library.libraryapp.repository.order;

import com.library.libraryapp.entity.purchase.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    @Query(value = """
                SELECT ROUND(CAST(SUM(total_amount) AS numeric), 2) FROM orders
                WHERE EXTRACT(MONTH FROM created_at) = EXTRACT(MONTH FROM CURRENT_DATE);
            """, nativeQuery = true)
    Optional<Double> getMontlyRevenue();

    @Query(value = "SELECT ROUND(AVG(CAST(total_amount AS numeric)), 2) FROM orders;", nativeQuery = true)
    Double getAverageOrderPrice();

}
