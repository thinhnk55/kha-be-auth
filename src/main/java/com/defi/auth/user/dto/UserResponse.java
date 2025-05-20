package com.defi.auth.user.dto;

public class UserResponse {
    private Long id;
    private String userName;

    private String fullName;

    private String email;

    private boolean emailVerified;

    private String phone;

    private boolean phoneVerified;

    private boolean locked;

    private Long lockedUntil;
}
