package com.defi.auth.user.controller;

import com.defi.auth.user.dto.*;
import com.defi.common.BaseResponse;
import com.defi.common.Pagination;
import com.defi.auth.user.entity.User;
import com.defi.auth.user.service.AdminUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/auth/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @PostMapping
    public ResponseEntity<BaseResponse<User>> create(
            @RequestBody @Valid AdminCreateUserRequest req
    ) {
        User user = adminUserService.createUser(req);
        return ResponseEntity.ok(BaseResponse.of(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<User>> get(@PathVariable Long id) {
        User user = adminUserService.getUser(id);
        return ResponseEntity.ok(BaseResponse.of(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<User>> update(@PathVariable Long id, @RequestBody UpdateUserRequest req) {
        User user = adminUserService.updateUser(id, req);
        return ResponseEntity.ok(BaseResponse.of(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        adminUserService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<?> updatePassword(@PathVariable Long id, @RequestBody UpdatePasswordRequest req) {
        adminUserService.updatePassword(id, req.getNewPassword());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/metadata")
    public ResponseEntity<?> updateMetadata(
            @PathVariable Long id,
            @RequestBody UpdateMetadataRequest req
    ) {
        adminUserService.updateMetadata(id, req.getMetadata());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/lock")
    public ResponseEntity<?> lock(@PathVariable Long id, @RequestBody LockAccountRequest req) {
        adminUserService.lockUser(id, req.isLocked(), req.getLockedUntil());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<User>>> listUsers(
            @PageableDefault Pageable pageable
    ) {
        List<User> users = adminUserService.listUsers(pageable);
        Pagination pagination = Pagination.of(pageable);
        return ResponseEntity.ok(BaseResponse.of(users, pagination));
    }
}
