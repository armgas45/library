package com.library.libraryapp.repository.book;

import com.library.libraryapp.entity.book.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {
    @Query(value = "SELECT * FROM books WHERE genre IN :genres LIMIT :limit ", nativeQuery = true)
    List<BookEntity> findByGenres(@Param("genres") List<String> preferredGenres,
                                  @Param("limit") int limit
    );

    @Query(value = """
                SELECT * FROM books
                WHERE id = (
                    SELECT op.book_id FROM orders
                    INNER JOIN ordered_books op on orders.id = op.order_id
                    WHERE EXTRACT(MONTH FROM created_at) = EXTRACT(MONTH FROM current_date)
                    GROUP BY op.book_id
                    ORDER BY count(book_id) DESC
                    LIMIT 1
                );""", nativeQuery = true)
    BookEntity getMontlyTopProduct();
}
