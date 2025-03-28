package asset.spy.auth.service.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class JwtService {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.expire-time-access-token}")
    private Long expireTime;

    public String generateAccessToken(String login, String role) {
        log.info("Generating access token for user with login: {}", login);

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(login)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expireTime))
                .and()
                .signWith(getKey())
                .compact();
    }

    public UUID generateRefreshToken() {
        log.info("Generating refresh token for user");

        return UUID.randomUUID();
    }

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}
