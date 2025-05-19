package com.defi.auth.role.dto;

import lombok.Data;

@Data
public class UserRoleInGroupRequest {
    private Long userId;
    private Long roleId;
    private Long groupId;
}
