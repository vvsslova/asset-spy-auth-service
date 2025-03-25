package asset.spy.auth.service.service;

import asset.spy.auth.service.dto.AuthenticationRequestDto;
import asset.spy.auth.service.dto.AuthenticationResponseDto;
import asset.spy.auth.service.dto.RefreshTokenRequestDto;
import asset.spy.auth.service.dto.RefreshTokenResponseDto;
import asset.spy.auth.service.dto.RegistryRequestDto;
import asset.spy.auth.service.dto.RegistryResponseDto;
import asset.spy.auth.service.entity.AccountEntity;
import asset.spy.auth.service.exception.EntityAlreadyExistsException;
import asset.spy.auth.service.mapper.RefreshTokenMapper;
import asset.spy.auth.service.repository.AccountRepository;
import asset.spy.auth.service.security.details.AccountDetails;
import asset.spy.auth.service.entity.TokenEntity;
import asset.spy.auth.service.entity.UserEntity;
import asset.spy.auth.service.mapper.AuthenticationMapper;
import asset.spy.auth.service.mapper.RegistryMapper;
import asset.spy.auth.service.repository.TokenRepository;
import asset.spy.auth.service.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TokenRepository tokenRepository;
    private final RegistryMapper registryMapper;
    private final JwtService jwtService;
    private final AuthenticationMapper authenticationMapper;
    private final AuthenticationManager authManager;
    private final RefreshTokenMapper refreshTokenMapper;

    @Value("${jwt.expire-time-refresh-token}")
    private Long expireTime;


    @Transactional
    public RegistryResponseDto registry(RegistryRequestDto request) {
        log.info("Registration user");

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new EntityAlreadyExistsException("User with username '" + request.getUsername() + "' already exists");
        }

        if (accountRepository.findByLogin(request.getLogin()).isPresent()) {
            throw new EntityAlreadyExistsException("Account with login '" + request.getLogin() + "' already exists");
        }

        UserEntity userToRegistry = registryMapper.toUserEntity(request);
        AccountEntity accountToRegistry = registryMapper.toAccountEntity(request, userToRegistry);
        userToRegistry.setAccount(accountToRegistry);

        log.info("Saving user and his account");
        UserEntity savedUser = userRepository.save(userToRegistry);
        log.info("Saving user and his account was successful");

        return registryMapper.toRegistryResponseDto(savedUser);
    }

    @Transactional
    public AuthenticationResponseDto login(AuthenticationRequestDto request) {
        log.info("Authentication user");

        Authentication auth =
                authManager.authenticate(new UsernamePasswordAuthenticationToken
                        (request.getLogin(), request.getPassword()));

        AccountDetails account = (AccountDetails) auth.getPrincipal();

        log.info("Generating tokens");
        String accessToken = getAccessToken(account.getUsername());
        UUID refreshToken = getRefreshToken();
        log.info("Tokens was successfully generated");

        TokenEntity userToken = authenticationMapper.toTokenEntity(account.getAccount(), refreshToken);
        setExpireTime(userToken, OffsetDateTime.now());
        log.info("Saving user token");
        tokenRepository.save(userToken);
        log.info("Token was successfully saved");

        return authenticationMapper.toAuthenticationResponseDto(accessToken, refreshToken);
    }

    @Transactional
    public RefreshTokenResponseDto refreshToken(RefreshTokenRequestDto request) {
        log.info("Refreshing token");

        Optional<TokenEntity> tokenToRefresh = tokenRepository.findByRefreshToken(request.getRefreshToken());

        if (tokenToRefresh.isEmpty()) {
            throw new EntityNotFoundException("Token '" + request.getRefreshToken() + "' is wrong");
        }

        TokenEntity token = tokenToRefresh.get();
        AccountEntity account = token.getAccount();

        log.info("Refreshing tokens");
        UUID refreshToken = getRefreshToken();
        token.setRefreshToken(refreshToken);
        setExpireTime(token, token.getExpireTime());
        String accessToken = getAccessToken(account.getLogin());
        log.info("Access token was successfully generated");
        log.info("Tokens was successfully refreshed");

        return refreshTokenMapper.toRefreshTokenResponseDto(accessToken, refreshToken);
    }

    private UUID getRefreshToken() {
        return jwtService.generateRefreshToken();
    }

    private String getAccessToken(String login) {
        return jwtService.generateAccessToken(login);
    }

    private void setExpireTime(TokenEntity token, OffsetDateTime time) {
        token.setExpireTime(time.plusDays(expireTime));
    }
}
