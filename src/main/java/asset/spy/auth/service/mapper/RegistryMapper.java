package asset.spy.auth.service.mapper;

import asset.spy.auth.service.constant.Role;
import asset.spy.auth.service.dto.RegistryRequestDto;
import asset.spy.auth.service.dto.RegistryResponseDto;
import asset.spy.auth.service.entity.AccountEntity;
import asset.spy.auth.service.entity.UserEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface RegistryMapper {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Mapping(target = "externalId", ignore = true)
    UserEntity toUserEntity(RegistryRequestDto registryRequestDto);

    @Mapping(target = "user", source = "userEntity")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    AccountEntity toAccountEntity(RegistryRequestDto registryRequestDto, UserEntity userEntity);

    @Mapping(target = "accountLogin", source = "userEntity.account.login")
    RegistryResponseDto toRegistryResponseDto(UserEntity userEntity);

    @AfterMapping
    default void setExternalId(@MappingTarget UserEntity userEntity) {
        userEntity.setExternalId(UUID.randomUUID());
    }

    @AfterMapping
    default void encodePassword(@MappingTarget AccountEntity accountEntity) {
        accountEntity.setPassword(encoder.encode(accountEntity.getPassword()));
    }

    @AfterMapping
    default void setDefaultRole(@MappingTarget AccountEntity accountEntity) {
        accountEntity.setRole(Role.getDefaultRole().toString());
    }
}
