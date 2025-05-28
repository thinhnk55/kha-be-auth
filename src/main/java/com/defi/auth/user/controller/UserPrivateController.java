package com.defi.auth.user.controller;

import com.defi.auth.user.service.PrivateUserService;
import com.defi.common.api.BaseResponse;
import com.defi.common.filter.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/v1/private/users")
@RequiredArgsConstructor
public class UserPrivateController {
    private final PrivateUserService privateUserService;

    @GetMapping("/logout")
    public ResponseEntity<BaseResponse<CustomUserPrincipal>> logout(
            @AuthenticationPrincipal CustomUserPrincipal principal) {
        privateUserService.logout(principal.getSessionId());
        return ResponseEntity.ok().build();
    }
}
