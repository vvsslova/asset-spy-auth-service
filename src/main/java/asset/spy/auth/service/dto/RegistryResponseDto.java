package asset.spy.auth.service.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class RegistryResponseDto {
    private UUID externalId;
    private String accountLogin;
    private String username;
    private String description;
    private LocalDate dateOfBirth;
    private OffsetDateTime createdAt;
}
