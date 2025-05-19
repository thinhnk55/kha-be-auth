package com.defi.auth.user.mapper;

import com.defi.auth.user.dto.AdminCreateUserRequest;
import com.defi.auth.user.dto.UpdateUserRequest;
import com.defi.auth.user.entity.User;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {

    @Mapping(target = "emailVerified", constant = "false")
    @Mapping(target = "phoneVerified", constant = "false")
    @Mapping(target = "locked", constant = "false")
    @Mapping(target = "lockedUntil", ignore = true)
    User toUser(AdminCreateUserRequest req);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUser(@MappingTarget User user, UpdateUserRequest req);
}
