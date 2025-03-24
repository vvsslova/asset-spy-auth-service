package asset.spy.auth.service.mapper;

import asset.spy.auth.service.dto.RefreshTokenResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface RefreshTokenMapper {

    @Mapping(target = "accessToken", source = "accessToken")
    @Mapping(target = "refreshToken", source = "refreshToken")
    RefreshTokenResponseDto toRefreshTokenResponseDto(String accessToken, UUID refreshToken);
}
