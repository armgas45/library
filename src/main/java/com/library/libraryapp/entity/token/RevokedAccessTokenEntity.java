package com.library.libraryapp.entity.token;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity
@Table(name = "revoked_tokens")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RevokedAccessTokenEntity {

    @Id
    UUID id;

    @Column(unique = true, nullable = false, length = 10000)
    private String accessToken;

    @Column(nullable = false)
    private long ttl;
}
