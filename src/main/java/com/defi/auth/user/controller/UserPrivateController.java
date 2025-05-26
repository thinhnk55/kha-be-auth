package com.defi.auth.user.controller;

import com.defi.auth.filter.CustomUserPrincipal;
import com.defi.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/v1/private/users")
@RequiredArgsConstructor
public class UserPrivateController {
    @GetMapping("/me")
    public ResponseEntity<BaseResponse<CustomUserPrincipal>> getCurrentUser(
            @AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.ok(BaseResponse.of((CustomUserPrincipal)principal));
    }
}
