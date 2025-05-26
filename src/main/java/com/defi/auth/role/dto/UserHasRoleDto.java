package com.defi.auth.role.dto;

import lombok.Data;

@Data
public class UserHasRoleDto {
    private Long userId;
    private Long roleId;
}
