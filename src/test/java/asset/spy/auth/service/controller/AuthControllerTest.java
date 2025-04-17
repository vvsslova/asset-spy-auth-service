package asset.spy.auth.service.controller;

import asset.spy.auth.service.Initializer;
import asset.spy.auth.service.dto.AuthenticationResponseDto;
import asset.spy.auth.service.dto.RefreshTokenRequestDto;
import asset.spy.auth.service.exception.EntityAlreadyExistsException;
import asset.spy.auth.service.repository.UserRepository;
import asset.spy.auth.service.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class AuthControllerTest extends Initializer {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthController authController;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @SneakyThrows
    public void registryUserIfRequestIsNotValidTest() {
        var registryRequest = notValidRegistryRequestDto;

        mockMvc.perform(post("/v1/auth/registry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registryRequest)))

                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[*].message", everyItem(not(emptyOrNullString()))))
                .andExpect(jsonPath("$[*].time", everyItem(notNullValue())))
                .andExpect(jsonPath("$[*].message", containsInAnyOrder(
                        "Username must be between 2 and 50 characters",
                        "Date of birth must be in the past",
                        "Login cannot be blank",
                        "Login must be between 6 and 70 characters")));
        assertThrows(ConstraintViolationException.class, () -> authController.registry(registryRequest));
    }

    @Test
    @SneakyThrows
    public void registryUserIfRegistryWasSuccessfulTest() {
        var registryRequest = validRegistryRequestDto;

        mockMvc.perform(post("/v1/auth/registry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registryRequest)))

                .andExpectAll(status().isCreated(),
                        jsonPath("$.username").value(registryRequest.getUsername()),
                        jsonPath("$.description").value(registryRequest.getDescription()),
                        jsonPath("$.accountLogin").value(registryRequest.getLogin()),
                        jsonPath("$.dateOfBirth").value(registryRequest.getDateOfBirth().toString()),
                        jsonPath("$.externalId").isNotEmpty(),
                        jsonPath("$.createdAt").isNotEmpty());
        assertThat(userRepository.findByUsername(registryRequest.getUsername())).isNotEmpty();
    }

    @Test
    @SneakyThrows
    // registry not successful because user with this username already exists
    public void registryUserIfRegistryNotSuccessfulTest() {
        registryDefaultUser();
        var registryRequest = duplicateUsernameRegistryRequestDto;

        mockMvc.perform(post("/v1/auth/registry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registryRequest)))

                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", not(emptyOrNullString())))
                .andExpect(jsonPath("$.time", notNullValue()))
                .andExpect(jsonPath("$.message").value("User with username '"
                        + registryRequest.getUsername() + "' already exists"));
        assertThrows(EntityAlreadyExistsException.class, () -> userService.registry(registryRequest));
    }

    @Test
    @SneakyThrows
    // registry not successful because account with this login already exists
    public void registryUserIfRegistryWasNotSuccessfulTest() {
        registryDefaultUser();
        var registryRequest = duplicateLoginRegistryRequestDto;

        mockMvc.perform(post("/v1/auth/registry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registryRequest)))

                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", not(emptyOrNullString())))
                .andExpect(jsonPath("$.time", notNullValue()))
                .andExpect(jsonPath("$.message").value("Account with login '"
                        + registryRequest.getLogin() + "' already exists"));
        assertThrows(EntityAlreadyExistsException.class, () -> userService.registry(registryRequest));
    }

    @Test
    @SneakyThrows
    public void loginUserIfRequestIsNotValidTest() {
        var loginRequest = notValidAuthenticationRequestDto;

        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))

                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[*].message", everyItem(not(emptyOrNullString()))))
                .andExpect(jsonPath("$[*].time", everyItem(notNullValue())))
                .andExpect(jsonPath("$[*].message", containsInAnyOrder(
                        "Password must be between 6 and 200 characters",
                        "Login must be between 6 and 70 characters",
                        "Password cannot be blank")));
    }

    @Test
    @SneakyThrows
    public void loginUserIfRequestWasSuccessfulTest() {
        registryDefaultUser();
        var loginRequest = validAuthenticationRequestDto;

        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))

                .andExpectAll(status().isOk(),
                        jsonPath("$.accessToken").isNotEmpty(),
                        jsonPath("$.refreshToken").isNotEmpty());
    }

    @Test
    @SneakyThrows
    public void loginUserIfAccountNotFoundTest() {
        var loginRequest = validAuthenticationRequestDto;

        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))

                .andExpectAll(status().isUnauthorized(),
                        jsonPath("$.message").isNotEmpty(),
                        jsonPath("$.message").value("Account with login '"
                                + loginRequest.getLogin() + "' not found"));
    }

    @Test
    @SneakyThrows
    public void loginUserIfPasswordNotValidTest() {
        registryDefaultUser();
        var loginRequest = invalidPasswordAuthenticationRequestDto;

        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))

                .andExpectAll(status().isUnauthorized(),
                        jsonPath("$.message").isNotEmpty(),
                        jsonPath("$.message").value("Bad credentials"));
    }

    @Test
    @SneakyThrows
    public void refreshTokenIfRefreshingWasSuccessfulTest() {
        var refreshTokenRequest = getValidRefreshTokenRequest();

        mockMvc.perform(post("/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshTokenRequest)))

                .andExpectAll(status().isOk(),
                        jsonPath("$.accessToken").isNotEmpty(),
                        jsonPath("$.refreshToken").isNotEmpty());
    }

    @Test
    @SneakyThrows
    public void refreshTokenIfTokenNotFoundTest() {
        var refreshTokenRequest = invalidRefreshTokenRequestDto;

        mockMvc.perform(post("/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshTokenRequest)))

                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Token '"
                        + refreshTokenRequest.getRefreshToken() + "' is wrong"));
        assertThrows(EntityNotFoundException.class, () -> userService.refreshToken(refreshTokenRequest));
    }

    protected void registryDefaultUser() {
        userService.registry(validRegistryRequestDto);
    }

    @SneakyThrows
    protected RefreshTokenRequestDto getValidRefreshTokenRequest() {
        registryDefaultUser();

        var loginResponse = mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validAuthenticationRequestDto)))
                .andReturn();

        var authenticationResponseDto = objectMapper.readValue(loginResponse.getResponse().getContentAsString(),
                AuthenticationResponseDto.class);

        var refreshTokenRequest = new RefreshTokenRequestDto();
        refreshTokenRequest.setRefreshToken(authenticationResponseDto.getRefreshToken());

        return refreshTokenRequest;
    }
}
