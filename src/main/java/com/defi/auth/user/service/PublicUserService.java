package com.defi.auth.user.service;

import com.defi.auth.user.dto.CreateUserRequest;
import com.defi.auth.user.dto.LoginUserRequest;
import com.defi.auth.user.dto.UserResponse;
import com.defi.auth.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PublicUserService {
    UserResponse register(CreateUserRequest req, String ipAddress, String userAgent);

    ResponseEntity<?> login(LoginUserRequest req, String ipAddress, String userAgent);

    List<User> searchUsers(String keyword, Pageable pageable);
}
