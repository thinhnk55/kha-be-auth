package com.defi.auth.permission.mapper;

import com.defi.auth.permission.dto.ResourceDto;
import com.defi.auth.permission.dto.ResourceRequest;
import com.defi.auth.permission.entity.Resource;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ResourceMapper {

    @Mapping(target = "created_at", source = "resource.createdAt")
    @Mapping(target = "updated_at", source = "resource.updatedAt")
    @Mapping(target = "actions", source = "actions")
    ResourceDto toDto(Resource resource, List<Long> actions);

    Resource toEntity(ResourceRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget Resource entity, ResourceRequest request);
}
