package asset.spy.auth.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(description = "DTO with access and refresh tokens")
public class AuthenticationResponseDto {

    @Schema(description = "Access token", example = "Some access token", requiredMode = Schema.RequiredMode.REQUIRED)
    private String accessToken;

    @Schema(description = "Refresh token", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID refreshToken;
}
