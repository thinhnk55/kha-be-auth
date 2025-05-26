package com.defi.auth.user.mapper;

import com.defi.auth.user.dto.CreateUserRequest;
import com.defi.auth.user.dto.UpdateUserRequest;
import com.defi.auth.user.dto.UserResponse;
import com.defi.auth.user.entity.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {

    @Mapping(target = "emailVerified", constant = "false")
    @Mapping(target = "phoneVerified", constant = "false")
    @Mapping(target = "locked", constant = "false")
    @Mapping(target = "lockedUntil", ignore = true)
    User toUser(CreateUserRequest req);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUser(@MappingTarget User user, UpdateUserRequest req);

    @Mapping(target = "roles", source = "roles")
    @Mapping(target = "groups", source = "groups")
    @Mapping(target = "accessToken", source = "accessToken")
    @Mapping(target = "refreshToken", source = "refreshToken")
    UserResponse toResponse(User user, List<Long> roles, List<Long> groups, String accessToken, String refreshToken);
}
