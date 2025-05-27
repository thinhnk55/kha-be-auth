package com.defi.auth.user.controller;

import com.defi.auth.user.dto.CreateUserRequest;
import com.defi.auth.user.dto.LoginUserRequest;
import com.defi.auth.user.dto.UserResponse;
import com.defi.auth.user.service.PublicUserService;
import com.defi.common.web.IpAddress;
import com.defi.common.web.UserAgent;
import com.defi.common.api.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/v1/public/users")
@RequiredArgsConstructor
public class UserPublicController {
    private final PublicUserService publicUserService;

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<UserResponse>> register(
            @RequestBody @Valid CreateUserRequest req,
            @IpAddress String ipAddress,
            @UserAgent String userAgent
    ) {
        UserResponse user = publicUserService.register(req, ipAddress, userAgent);
        return ResponseEntity.ok(BaseResponse.of(user));
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody @Valid LoginUserRequest req,
            @IpAddress String ipAddress,
            @UserAgent String userAgent
    ) {
        return publicUserService.login(req, ipAddress, userAgent);
    }
}
