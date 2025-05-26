package com.defi.auth.user.dto;

import java.util.List;

public class FullUserResponse {
    private Long id;
    private String userName;

    private String fullName;
    private String email;
    private boolean emailVerified;
    private String phone;
    private boolean phoneVerified;

    private boolean locked;
    private Long lockedUntil;
    private List<Long> roles;
    private List<Long> groups;
    private String access_token;
    private String refresh_token;
}
