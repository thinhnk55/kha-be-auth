package com.defi.auth.user.dto;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private JsonObject metadata;
    private List<Long> roles;
    private List<Long> groups;
    private String accessToken;
    private String refreshToken;
}
