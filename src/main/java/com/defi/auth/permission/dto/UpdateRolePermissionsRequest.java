package com.defi.auth.permission.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

import com.defi.common.api.CommonMessage;

@Data
public class UpdateRolePermissionsRequest {
    @NotNull(message = CommonMessage.INVALID)
    private Long roleId;

    @NotNull(message = CommonMessage.INVALID)
    private List<ResourcePermission> resourcePermissions;

    @Data
    public static class ResourcePermission {
        @NotNull(message = CommonMessage.INVALID)
        private Long resourceId;

        @NotNull(message = CommonMessage.INVALID)
        private List<Long> actionIds;
    }
} 