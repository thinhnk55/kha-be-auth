package com.defi.auth.permission.mapper;

import com.defi.auth.permission.dto.PermissionRequest;
import com.defi.auth.permission.entity.Permission;
import org.mapstruct.*;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PermissionMapper {
    Permission toEntity(PermissionRequest request);
}
