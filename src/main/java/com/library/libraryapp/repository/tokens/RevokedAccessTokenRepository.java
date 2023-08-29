package com.library.libraryapp.repository.tokens;

import com.library.libraryapp.entity.token.RevokedAccessTokenEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface RevokedAccessTokenRepository extends JpaRepository<RevokedAccessTokenEntity, UUID> {

    @Query(value = "DELETE FROM revoked_tokens WHERE ttl < :currentDate", nativeQuery = true)
    @Modifying
    @Transactional
    void deleteExpiredTokens(@Param("currentDate") long currentDate);
}
