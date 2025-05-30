package com.defi.auth.user.controller;

import com.defi.auth.user.dto.CreateUserRequest;
import com.defi.auth.user.dto.LoginUserRequest;
import com.defi.auth.user.dto.UserResponse;
import com.defi.auth.user.entity.User;
import com.defi.auth.user.service.PublicUserService;
import com.defi.common.web.IpAddress;
import com.defi.common.web.UserAgent;
import com.defi.common.api.BaseResponse;
import com.defi.common.api.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth/v1/public/users")
@RequiredArgsConstructor
public class UserPublicController {
    private final PublicUserService publicUserService;

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<UserResponse>> register(
            @RequestBody @Valid CreateUserRequest req,
            @IpAddress String ipAddress,
            @UserAgent String userAgent) {
        UserResponse user = publicUserService.register(req, ipAddress, userAgent);
        return ResponseEntity.ok(BaseResponse.of(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody @Valid LoginUserRequest req,
            @IpAddress String ipAddress,
            @UserAgent String userAgent) {
        return publicUserService.login(req, ipAddress, userAgent);
    }

    @GetMapping("/filter")
    public ResponseEntity<BaseResponse<List<User>>> searchUsers(
            @RequestParam(required = false) String keyword,
            @PageableDefault Pageable pageable) {
        List<User> users = publicUserService.searchUsers(keyword, pageable);
        Pagination pagination = Pagination.of(pageable);
        return ResponseEntity.ok(BaseResponse.of(users, pagination));
    }
}
