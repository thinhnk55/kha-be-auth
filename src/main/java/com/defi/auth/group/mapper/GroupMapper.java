package com.defi.auth.group.mapper;

import com.defi.auth.group.dto.CreateGroupRequest;
import com.defi.auth.group.entity.Group;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GroupMapper {
    @Mapping(target = "id", ignore = true)
    Group fromCreateRequest(CreateGroupRequest req);
}
