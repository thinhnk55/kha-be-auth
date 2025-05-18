package com.defi.auth.user.controller;

import com.defi.auth.user.dto.AdminCreateUserRequest;
import com.defi.auth.user.dto.AdminUpdateUserRequest;
import com.defi.auth.user.dto.LockAccountRequest;
import com.defi.auth.user.dto.UpdatePasswordRequest;
import com.defi.auth.user.entity.User;
import com.defi.auth.user.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @PostMapping
    public ResponseEntity<User> create(@RequestBody AdminCreateUserRequest req) {
        return ResponseEntity.ok(adminUserService.createUser(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> get(@PathVariable Long id) {
        return ResponseEntity.ok(adminUserService.getUser(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody AdminUpdateUserRequest req) {
        return ResponseEntity.ok(adminUserService.updateUser(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        adminUserService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @RequestBody UpdatePasswordRequest req) {
        adminUserService.updatePassword(id, req.getNewPassword());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/lock")
    public ResponseEntity<Void> lock(@PathVariable Long id, @RequestBody LockAccountRequest req) {
        adminUserService.lockUser(id, req.isLocked(), req.getLockedUntil());
        return ResponseEntity.ok().build();
    }
}
