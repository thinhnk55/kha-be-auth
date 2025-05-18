package com.defi.auth.user.dto;

import lombok.Data;

@Data
public class UpdatePasswordRequest {
    private String newPassword;
}