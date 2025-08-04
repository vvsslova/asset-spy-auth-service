package asset.spy.auth.service.open.api.rest;

import asset.spy.auth.service.dto.AuthenticationRequestDto;
import asset.spy.auth.service.dto.AuthenticationResponseDto;
import asset.spy.auth.service.dto.ErrorResponseDto;
import asset.spy.auth.service.dto.RefreshTokenRequestDto;
import asset.spy.auth.service.dto.RefreshTokenResponseDto;
import asset.spy.auth.service.dto.RegistryRequestDto;
import asset.spy.auth.service.dto.RegistryResponseDto;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Auth", description = "Auth API")
@ApiResponse(responseCode = "500", description = "Internal server error", content = {
        @Content(mediaType = "application/json", schema =
        @Schema(implementation = ErrorResponseDto.class))
})
public interface AuthOpenApi {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "OK - User is registered", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = RegistryResponseDto.class))
            }),
            @ApiResponse(responseCode = "400", description = "Bad request", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ErrorResponseDto.class))
            }),
            @ApiResponse(responseCode = "409", description = "Account with this login or " +
                    "user with this username already exists", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ErrorResponseDto.class))
            })
    })
    RegistryResponseDto registry(@Valid @RequestBody RegistryRequestDto registryRequestDto);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = AuthenticationResponseDto.class))
            }),
            @ApiResponse(responseCode = "400", description = "Bad request", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ErrorResponseDto.class))
            }),
            @ApiResponse(responseCode = "401", description = "Authorization failed - bad credentials", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ErrorResponseDto.class))
            })
    })
    AuthenticationResponseDto login(@Valid @RequestBody AuthenticationRequestDto authenticationRequestDto);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = RefreshTokenResponseDto.class))
            }),
            @ApiResponse(responseCode = "404", description = "Token is expired. Authorization is required", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ErrorResponseDto.class))
            })
    })
    RefreshTokenResponseDto refreshToken(@NotNull @RequestBody RefreshTokenRequestDto request);
}
