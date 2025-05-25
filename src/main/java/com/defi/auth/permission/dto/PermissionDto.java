package com.defi.auth.permission.dto;

import lombok.Data;

@Data
public class PermissionDto {
    private Long id;
    private Long roleId;
    private Long resourceId;
    private Long actionId;
}
