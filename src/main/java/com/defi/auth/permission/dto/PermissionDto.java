package com.defi.auth.permission.dto;

import lombok.Data;

@Data
public class PermissionDto {
    private Long id;
    private Long created_at;
    private Long updated_at;
    private Long roleId;
    private Long groupId;
    private Long resourceId;
    private Long actionId;
}
