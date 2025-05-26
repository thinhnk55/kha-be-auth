package com.defi.auth.permission.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PermissionRequest {
    @NotNull
    private Long roleId;

    @NotNull
    private Long resourceId;

    @NotNull
    private Long actionId;
}
