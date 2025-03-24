package asset.spy.auth.service.service;

import asset.spy.auth.service.repository.TokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@AllArgsConstructor
@Slf4j
public class RefreshTokenCleanupService {

    private final TokenRepository tokenRepository;

    @Scheduled(fixedRateString = "${scheduling.fixed-rate}")
    @Transactional
    public void deleteExpiredTokens() {
        log.info("Cleaning up expired tokens");

        tokenRepository.deleteByExpireTimeIsBefore(OffsetDateTime.now());

        log.info("Cleaning up expired tokens completed");
    }
}
