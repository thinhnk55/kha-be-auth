package com.defi.auth.permission.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceHasActionId implements Serializable {
    private Long resourceId;
    private Long actionId;
}
