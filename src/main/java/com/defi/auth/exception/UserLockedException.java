package com.defi.auth.exception;

import com.defi.auth.user.dto.LockedUserDto;
import lombok.Getter;

@Getter
public class UserLockedException extends RuntimeException {
    private final LockedUserDto lockedUserDto;
    public UserLockedException(String message, LockedUserDto lockedUserDto) {
        super(message);
        this.lockedUserDto = lockedUserDto;
    }
}
