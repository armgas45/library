package com.library.libraryapp.service.auth.token;

import com.library.libraryapp.domain.core.token.TokenService;
import com.library.libraryapp.entity.token.RefreshTokenEntity;
import com.library.libraryapp.entity.token.RevokedAccessTokenEntity;
import com.library.libraryapp.exception.errors.UnknownRefreshTokenException;
import com.library.libraryapp.repository.tokens.RefreshTokenRepository;
import com.library.libraryapp.repository.tokens.RevokedAccessTokenRepository;
import com.library.libraryapp.util.jwt.JwtUtil;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import java.util.Collection;

import static com.library.libraryapp.util.StringUtils.isNullOrEmpty;

@Service
@AllArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final RevokedAccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    @Override
    public String issueAccessToken(String username, Collection<? extends GrantedAuthority> roles) {
        if (isNullOrEmpty(username) || roles == null) throw new IllegalArgumentException();

        return jwtUtil.getJwtToken(username, roles);
    }

    @Override
    public String issueRefreshToken(String username, Collection<? extends GrantedAuthority> roles) {
        if (isNullOrEmpty(username) || roles == null) throw new IllegalArgumentException();

        var refreshToken = jwtUtil.getRefreshToken(username, roles);
        var id = jwtUtil.extractTokenId(refreshToken);
        var ttl = jwtUtil.extractExpiration(refreshToken);

        refreshTokenRepository.save(new RefreshTokenEntity(id, refreshToken, ttl));
        return refreshToken;
    }

    @Override
    public String issueAccessTokenByRefreshToken(String refreshToken) {
        if (isNullOrEmpty(refreshToken)) throw new IllegalArgumentException();

        if (!jwtUtil.isRefreshToken(refreshToken) || !jwtUtil.validateToken(refreshToken))
            throw new JwtException("Invalid Refresh Token");

        var tokenId = jwtUtil.extractTokenId(refreshToken);

        if (refreshTokenRepository.existsById(tokenId)) {
            var username = jwtUtil.extractUsername(refreshToken);
            var roles = jwtUtil.extractRoles(refreshToken)
                    .stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();

            return jwtUtil.getJwtToken(username, roles);
        }

        throw new UnknownRefreshTokenException();
    }

    @Override
    public boolean validateAccessToken(String accessToken) {
        if (isNullOrEmpty(accessToken)) return false;

        return jwtUtil.validateToken(accessToken) && !accessTokenRepository.existsById(jwtUtil.extractTokenId(accessToken));
    }

    @Override
    public void revokeAccessToken(String accessToken) {
        if (isNullOrEmpty(accessToken)) throw new IllegalArgumentException();

        var revokedToken = RevokedAccessTokenEntity.builder()
                .id(jwtUtil.extractTokenId(accessToken))
                .accessToken(accessToken)
                .ttl(jwtUtil.extractExpiration(accessToken))
                .build();

       accessTokenRepository.save(revokedToken);
    }

    @Override
    public void revokeRefreshToken(String refreshToken) {
        if (isNullOrEmpty(refreshToken)) throw new IllegalArgumentException();

        refreshTokenRepository.deleteById(jwtUtil.extractTokenId(refreshToken));
    }
}
