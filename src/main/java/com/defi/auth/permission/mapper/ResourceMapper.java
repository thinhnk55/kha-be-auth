package com.defi.auth.permission.mapper;

import com.defi.auth.permission.dto.ResourceDto;
import com.defi.auth.permission.dto.ResourceRequest;
import com.defi.auth.permission.entity.Resource;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ResourceMapper {

    ResourceDto toDto(Resource resource, List<Long> actions);
    Resource toEntity(ResourceRequest request);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget Resource entity, ResourceRequest request);
}
