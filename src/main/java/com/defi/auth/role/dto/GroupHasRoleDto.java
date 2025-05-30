package com.defi.auth.role.dto;

import lombok.Data;

@Data
public class GroupHasRoleDto {
    private Long groupId;
    private Long roleId;
}