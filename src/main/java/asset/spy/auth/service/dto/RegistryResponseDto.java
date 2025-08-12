package asset.spy.auth.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Schema(description = "DTO of registered user")
public class RegistryResponseDto {

    @Schema(description = "ID of user", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID externalId;

    @Schema(description = "Login", example = "Some login", requiredMode = Schema.RequiredMode.REQUIRED)
    private String accountLogin;

    @Schema(description = "Username", example = "Some username", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @Schema(description = "Description", example = "Some description", requiredMode = Schema.RequiredMode.REQUIRED)
    private String description;

    @Schema(description = "Date of birth", example = "2000-09-12", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate dateOfBirth;

    @Schema(description = "Time of creation", requiredMode = Schema.RequiredMode.REQUIRED)
    private OffsetDateTime createdAt;
}
