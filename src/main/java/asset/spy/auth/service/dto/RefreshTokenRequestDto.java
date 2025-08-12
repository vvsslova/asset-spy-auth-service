package asset.spy.auth.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(description = "DTO for refreshing tokens")
public class RefreshTokenRequestDto {

    @NotNull(message = "Token cannot be null")
    @Schema(description = "Refresh token")
    private UUID refreshToken;
}
