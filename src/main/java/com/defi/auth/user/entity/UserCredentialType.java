package com.defi.auth.user.entity;

import lombok.Getter;

@Getter
public enum UserCredentialType {
    PASSWORD("password"),
    OTP("otp");

    private final String value;

    UserCredentialType(String value) {
        this.value = value;
    }
}
