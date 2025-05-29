package com.defi.auth.permission.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRolePermissionsResponse {
    private Long roleId;
    private Integer totalPermissions;
    private String message;
    
    public static UpdateRolePermissionsResponse success(Long roleId, Integer totalPermissions) {
        return UpdateRolePermissionsResponse.builder()
                .roleId(roleId)
                .totalPermissions(totalPermissions)
                .message("Role permissions updated successfully")
                .build();
    }
} 