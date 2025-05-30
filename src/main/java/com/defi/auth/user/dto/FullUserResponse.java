package com.defi.auth.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
