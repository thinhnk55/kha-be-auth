package com.defi.auth.user.dto;

import lombok.Data;

@Data
public class AdminCreateUserRequest {
    private String userName;
    private String fullName;
    private String email;
    private String password;
}


