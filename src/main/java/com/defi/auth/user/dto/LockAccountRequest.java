package com.defi.auth.user.dto;

import lombok.Data;

@Data
public class LockAccountRequest {
    private boolean locked;
    private Long lockedUntil;
}