package com.defi.auth.permission.mapper;

import com.defi.auth.permission.dto.PermissionDto;
import com.defi.auth.permission.dto.PermissionRequest;
import com.defi.auth.permission.entity.Permission;
import org.mapstruct.*;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PermissionMapper {
    Permission toEntity(PermissionRequest request);

    PermissionDto toDto(Permission permission);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget Permission entity, PermissionRequest request);
}
