package com.defi.auth.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LockedUserDto {
    private boolean locked;
    private long lockedUntil;
}
