package asset.spy.auth.service.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class RefreshTokenResponseDto {
    private String accessToken;
    private UUID refreshToken;
}
