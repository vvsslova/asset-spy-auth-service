package asset.spy.auth.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "DTO for authentication of user")
public class AuthenticationRequestDto {

    @NotBlank(message = "Login cannot be blank")
    @Size(min = 6, max = 70, message = "Login must be between 6 and 70 characters")
    @Schema(description = "Login", example = "Some login")
    private String login;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 200, message = "Password must be between 6 and 200 characters")
    @Schema(description = "Password", example = "Some password")
    private String password;
}
