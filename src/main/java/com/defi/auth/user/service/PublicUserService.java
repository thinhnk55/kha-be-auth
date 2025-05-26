package com.defi.auth.user.service;

import com.defi.auth.user.dto.CreateUserRequest;
import com.defi.auth.user.dto.LoginUserRequest;
import com.defi.auth.user.dto.UserResponse;
import org.springframework.http.ResponseEntity;

public interface PublicUserService {
    UserResponse register(CreateUserRequest req, String ipAddress, String userAgent);

    ResponseEntity<?> login(LoginUserRequest req, String ipAddress, String userAgent);
}
