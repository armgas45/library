package com.library.libraryapp.config.logout;

import com.library.libraryapp.domain.constants.RequestHeaders;
import com.library.libraryapp.domain.core.token.TokenService;
import com.library.libraryapp.util.jwt.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import static com.library.libraryapp.util.StringUtils.isNullOrEmpty;

@Component
@AllArgsConstructor
public class ApplicationLogoutHandler implements LogoutHandler {
    private final JwtUtil jwtUtil;
    private final TokenService tokenService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (!request.getMethod().equals(HttpMethod.POST.name())) {
            response.setStatus(403);
            return;
        }

        var authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        var refreshToken = request.getHeader(RequestHeaders.REFRESH_TOKEN);

        if (isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith("Bearer ") || isNullOrEmpty(refreshToken)) {
            response.setStatus(403);
            return;
        }

        var accessToken = jwtUtil.removeBearerFromToken(authorizationHeader);

        try {
            tokenService.revokeAccessToken(accessToken);
            tokenService.revokeRefreshToken(refreshToken);
        } catch (JwtException e) {
            response.setStatus(401);
            return;
        }

        response.setStatus(200);
    }
}
