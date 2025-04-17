package asset.spy.auth.service;

import asset.spy.auth.service.dto.AuthenticationRequestDto;
import asset.spy.auth.service.dto.RefreshTokenRequestDto;
import asset.spy.auth.service.dto.RegistryRequestDto;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.time.LocalDate;
import java.util.UUID;

public abstract class Initializer {

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("asset_spy_db_test")
            .withUsername("postgres")
            .withPassword("123")
            .withInitScript("initial_test_DB.sql");

    protected static RegistryRequestDto notValidRegistryRequestDto;
    protected static RegistryRequestDto validRegistryRequestDto;
    protected static RegistryRequestDto duplicateUsernameRegistryRequestDto;
    protected static RegistryRequestDto duplicateLoginRegistryRequestDto;
    protected static AuthenticationRequestDto notValidAuthenticationRequestDto;
    protected static AuthenticationRequestDto validAuthenticationRequestDto;
    protected static AuthenticationRequestDto invalidPasswordAuthenticationRequestDto;
    protected static RefreshTokenRequestDto invalidRefreshTokenRequestDto;

    @BeforeAll
    static void init() {
        notValidRegistryRequestDto = new RegistryRequestDto();
        notValidRegistryRequestDto.setUsername("3");
        notValidRegistryRequestDto.setLogin("");
        notValidRegistryRequestDto.setPassword("password123");
        notValidRegistryRequestDto.setDescription("description");
        notValidRegistryRequestDto.setDateOfBirth((LocalDate.of(2026, 1, 13)));

        validRegistryRequestDto = new RegistryRequestDto();
        validRegistryRequestDto.setUsername("test user");
        validRegistryRequestDto.setLogin("test login");
        validRegistryRequestDto.setPassword("password 123");
        validRegistryRequestDto.setDescription("description");
        validRegistryRequestDto.setDateOfBirth((LocalDate.of(1999, 1, 13)));

        duplicateUsernameRegistryRequestDto = new RegistryRequestDto();
        duplicateUsernameRegistryRequestDto.setUsername("test user");
        duplicateUsernameRegistryRequestDto.setLogin("test login");
        duplicateUsernameRegistryRequestDto.setPassword("password123");
        duplicateUsernameRegistryRequestDto.setDescription("description");
        duplicateUsernameRegistryRequestDto.setDateOfBirth((LocalDate.of(1999, 1, 13)));

        duplicateLoginRegistryRequestDto = new RegistryRequestDto();
        duplicateLoginRegistryRequestDto.setUsername("test user 2");
        duplicateLoginRegistryRequestDto.setLogin("test login");
        duplicateLoginRegistryRequestDto.setPassword("password123");
        duplicateLoginRegistryRequestDto.setDescription("description");
        duplicateLoginRegistryRequestDto.setDateOfBirth((LocalDate.of(1999, 1, 13)));

        notValidAuthenticationRequestDto = new AuthenticationRequestDto();
        notValidAuthenticationRequestDto.setLogin("l");
        notValidAuthenticationRequestDto.setPassword("");

        validAuthenticationRequestDto = new AuthenticationRequestDto();
        validAuthenticationRequestDto.setLogin("test login");
        validAuthenticationRequestDto.setPassword("password 123");

        invalidPasswordAuthenticationRequestDto = new AuthenticationRequestDto();
        invalidPasswordAuthenticationRequestDto.setLogin("test login");
        invalidPasswordAuthenticationRequestDto.setPassword("password12");

        invalidRefreshTokenRequestDto = new RefreshTokenRequestDto();
        invalidRefreshTokenRequestDto.setRefreshToken(UUID.randomUUID());
    }
}
