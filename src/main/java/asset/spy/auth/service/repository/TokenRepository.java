package asset.spy.auth.service.repository;

import asset.spy.auth.service.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
    void deleteByExpireTimeIsBefore(OffsetDateTime expireTime);

    Optional<TokenEntity> findByRefreshToken(UUID refreshToken);
}
