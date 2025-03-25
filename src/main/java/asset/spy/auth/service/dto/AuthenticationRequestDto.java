package asset.spy.auth.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthenticationRequestDto {

    @NotBlank(message = "Login cannot be blank")
    @Size(min = 6, max = 70, message = "Login must be between 6 and 70 characters")
    private String login;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 200, message = "Password must be between 6 and 200 characters")
    private String password;
}
