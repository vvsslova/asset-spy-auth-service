package asset.spy.auth.service.controller;

import asset.spy.auth.service.dto.AuthenticationRequestDto;
import asset.spy.auth.service.dto.AuthenticationResponseDto;
import asset.spy.auth.service.dto.RefreshTokenRequestDto;
import asset.spy.auth.service.dto.RefreshTokenResponseDto;
import asset.spy.auth.service.dto.RegistryRequestDto;
import asset.spy.auth.service.dto.RegistryResponseDto;
import asset.spy.auth.service.open.api.rest.AuthOpenApi;
import asset.spy.auth.service.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController implements AuthOpenApi {
    private final UserService userService;

    @PostMapping("/registry")
    @ResponseStatus(HttpStatus.CREATED)
    public RegistryResponseDto registry(@Valid @RequestBody RegistryRequestDto registryRequestDto) {
        return userService.registry(registryRequestDto);
    }

    @PostMapping("/login")
    public AuthenticationResponseDto login(@Valid @RequestBody AuthenticationRequestDto authenticationRequestDto) {
        return userService.login(authenticationRequestDto);
    }

    @PostMapping("/refresh")
    public RefreshTokenResponseDto refreshToken(@NotNull @RequestBody RefreshTokenRequestDto request) {
        return userService.refreshToken(request);
    }
}
