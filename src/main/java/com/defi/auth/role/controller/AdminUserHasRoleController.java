package com.defi.auth.role.controller;

import com.defi.auth.role.dto.UserHasRoleDto;
import com.defi.auth.role.entity.UserHasRole;
import com.defi.auth.role.service.UserHasRoleService;
import com.defi.common.api.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth/v1/admin/user-has-role")
@RequiredArgsConstructor
public class AdminUserHasRoleController {

    private final UserHasRoleService userHasRoleService;

    @GetMapping("/{userId}/{roleId}")
    public ResponseEntity<BaseResponse<UserHasRole>> getUserHasRole(
            @PathVariable Long userId,
            @PathVariable Long roleId
    ) {
        UserHasRole userHasRole = userHasRoleService.getUserHasRole(userId, roleId);
        return ResponseEntity.ok(BaseResponse.of(userHasRole));
    }

    @PostMapping
    public ResponseEntity<BaseResponse<?>> assignRoleToUser(
            @RequestBody UserHasRoleDto request
    ) {
        userHasRoleService.assignRoleToUser(request.getUserId(), request.getRoleId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<BaseResponse<?>> removeRoleFromUser(
            @RequestBody UserHasRoleDto request
    ) {
        userHasRoleService.removeRoleFromUser(request.getUserId(), request.getRoleId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/by-user")
    public ResponseEntity<BaseResponse<List<UserHasRole>>> findAllByIdUserId(
            @RequestParam Long userId
    ) {
        List<UserHasRole> list = userHasRoleService.findAllByIdUserId(userId);
        return ResponseEntity.ok(BaseResponse.of(list));
    }

    @GetMapping("/by-role")
    public ResponseEntity<BaseResponse<List<UserHasRole>>> findAllByIdRoleId(
            @RequestParam Long groupId
    ) {
        List<UserHasRole> list = userHasRoleService.findAllByIdRoleId(groupId);
        return ResponseEntity.ok(BaseResponse.of(list));
    }
}
