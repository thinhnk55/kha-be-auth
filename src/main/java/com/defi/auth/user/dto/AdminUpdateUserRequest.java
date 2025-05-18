package com.defi.auth.user.dto;

import lombok.Data;

@Data
public class AdminUpdateUserRequest {
    private String fullName;
    private String email;
    private String phone;
}