package com.library.libraryapp.domain.core.token;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

public interface TokenService {
    String issueAccessToken(String username, Collection<? extends GrantedAuthority> roles);
    String issueRefreshToken(String username, Collection<? extends GrantedAuthority> roles);
    String issueAccessTokenByRefreshToken(String refreshToken);
    boolean validateAccessToken(String accessToken);
    void revokeAccessToken(String accessToken);
    void revokeRefreshToken(String refreshToken);
}

