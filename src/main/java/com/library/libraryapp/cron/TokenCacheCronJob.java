package com.library.libraryapp.cron;

import com.library.libraryapp.repository.tokens.RefreshTokenRepository;
import com.library.libraryapp.repository.tokens.RevokedAccessTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Component
@AllArgsConstructor
public class TokenCacheCronJob {
    private final RefreshTokenRepository refreshTokenRepository;
    private final RevokedAccessTokenRepository revokedAccessTokenRepository;

    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.DAYS)
    public void clearCache() {
        refreshTokenRepository.deleteExpiredTokens(System.currentTimeMillis());
        revokedAccessTokenRepository.deleteExpiredTokens(System.currentTimeMillis());
    }
}
