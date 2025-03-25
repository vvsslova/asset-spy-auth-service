package asset.spy.auth.service.mapper;

import asset.spy.auth.service.dto.AuthenticationResponseDto;
import asset.spy.auth.service.entity.AccountEntity;
import asset.spy.auth.service.entity.TokenEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface AuthenticationMapper {

    @Mapping(target = "account", source = "accountEntity")
    @Mapping(target = "refreshToken", source = "refreshToken")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "expireTime", ignore = true)
    TokenEntity toTokenEntity(AccountEntity accountEntity, UUID refreshToken);

    @Mapping(target = "refreshToken", source = "refreshToken")
    @Mapping(target = "accessToken", source = "accessToken")
    AuthenticationResponseDto toAuthenticationResponseDto(String accessToken, UUID refreshToken);
}
