package com.defi.auth.permission.mapper;

import com.defi.auth.permission.dto.ActionRequest;
import com.defi.auth.permission.entity.Action;
import org.mapstruct.*;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ActionMapper {
    Action toEntity(ActionRequest request);
    void updateEntity(@MappingTarget Action existing, ActionRequest request);
}
