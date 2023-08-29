package com.library.libraryapp.util.jwt;

import com.library.libraryapp.domain.constants.TokenTypes;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;

import static com.library.libraryapp.util.StringUtils.isNullOrEmpty;

@Component
public class JwtUtil {

    // should be inserted from ENV
    private final SecretKey secretKey = Keys.hmacShaKeyFor("LIBRARY_APP_GLOBAL_JWT_TOKEN_SECRET_LIBRARY_APP_GLOBAL_JWT_TOKEN_SECRET".getBytes(StandardCharsets.UTF_8));
    private final long ONE_DAY = 1000 * 60 * 60 * 24;
    private final long TEN_DAYS = 10 * ONE_DAY;


    public String getJwtToken(String username, Collection<? extends GrantedAuthority> roles) {
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ONE_DAY))
                .setSubject(username)
                .claim("roles", roles)
                .claim("token-type", TokenTypes.ACCESS_TOKEN)
                .claim("token-id", UUID.randomUUID())
                .signWith(secretKey)
                .compact();
    }

    public String getRefreshToken(String username, Collection<? extends GrantedAuthority> roles) {
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TEN_DAYS))
                .setSubject(username)
                .claim("roles", roles)
                .claim("token-type", TokenTypes.REFRESH_TOKEN)
                .claim("token-id", UUID.randomUUID())
                .signWith(secretKey)
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsFunction) {
        var claims = extractAllClaims(token);
        return claimsFunction.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public long extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration).getTime();
    }

    public List<String> extractRoles(String token) {
        var authorities = new ArrayList<String>();
        var roles = extractClaim(token, claims -> (List<LinkedHashMap<String, String>>) claims.get("roles"));

        roles.forEach(authority -> authorities.add(authority.get("authority")));
        return authorities;
    }

    public UUID extractTokenId(String token) {
        if (isNullOrEmpty(token)) return null;

        return UUID.fromString((String) extractClaim(token, claims -> claims.get("token-id")));
    }

    public boolean isExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public String removeBearerFromToken(String token) {
        if (isNullOrEmpty(token) || !token.startsWith("Bearer")) return "";
        return token.split(" ")[1];
    }

    public boolean validateToken(String token) {
        return !isExpired(token);
    }

    public boolean isAccessToken(String accessToken) {
        var tokenType = (String) extractClaim(accessToken, claims -> claims.get("token-type"));
        return TokenTypes.ACCESS_TOKEN.name().equals(tokenType);
    }

    public boolean isRefreshToken(String refreshToken) {
        var tokenType = (String) extractClaim(refreshToken, claims -> claims.get("token-type"));
        return TokenTypes.REFRESH_TOKEN.name().equals(tokenType);
    }
}
