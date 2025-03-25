package asset.spy.auth.service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class RefreshTokenRequestDto {

    @NotNull(message = "Token cannot be null")
    private UUID refreshToken;
}
