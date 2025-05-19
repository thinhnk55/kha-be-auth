package com.defi.auth.role.controller;

import com.defi.auth.role.dto.UserRoleInGroupRequest;
import com.defi.auth.role.entity.UserRoleInGroup;
import com.defi.auth.role.service.UserRoleInGroupService;
import com.defi.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth/admin/user-role-in-group")
@RequiredArgsConstructor
public class AdminUserRoleInGroupController {

    private final UserRoleInGroupService userRoleInGroupService;

    @PostMapping
    public ResponseEntity<BaseResponse<?>> addUserRoleInGroup(
            @RequestBody UserRoleInGroupRequest request
    ) {
        userRoleInGroupService.addUserRoleInGroup(request.getUserId(), request.getRoleId(), request.getGroupId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<BaseResponse<?>> removeUserRoleInGroup(
            @RequestBody UserRoleInGroupRequest request
    ) {
        userRoleInGroupService.removeUserRoleInGroup(request.getUserId(), request.getRoleId(), request.getGroupId());
        return ResponseEntity.ok().build();
    }

    @GetMapping()
    public ResponseEntity<BaseResponse<List<UserRoleInGroup>>> findAll() {
        List<UserRoleInGroup> list = userRoleInGroupService.findAll();
        return ResponseEntity.ok(BaseResponse.of(list));
    }

    @GetMapping("/by-user")
    public ResponseEntity<BaseResponse<List<UserRoleInGroup>>> findAllByIdUserId(
            @RequestParam Long userId
    ) {
        List<UserRoleInGroup> list = userRoleInGroupService.findAllByIdUserId(userId);
        return ResponseEntity.ok(BaseResponse.of(list));
    }

    @GetMapping("/by-group")
    public ResponseEntity<BaseResponse<List<UserRoleInGroup>>> findAllByIdGroupId(
            @RequestParam Long groupId
    ) {
        List<UserRoleInGroup> list = userRoleInGroupService.findAllByIdGroupId(groupId);
        return ResponseEntity.ok(BaseResponse.of(list));
    }

    @GetMapping("/by-role")
    public ResponseEntity<BaseResponse<List<UserRoleInGroup>>> findAllByIdRoleId(
            @RequestParam Long groupId
    ) {
        List<UserRoleInGroup> list = userRoleInGroupService.findAllByIdRoleId(groupId);
        return ResponseEntity.ok(BaseResponse.of(list));
    }

    @GetMapping("/by-user-group")
    public ResponseEntity<BaseResponse<List<UserRoleInGroup>>> getUserRolesInGroup(
            @RequestParam Long userId,
            @RequestParam Long groupId
    ) {
        List<UserRoleInGroup> list = userRoleInGroupService.getUserRolesInGroup(userId, groupId);
        return ResponseEntity.ok(BaseResponse.of(list));
    }
}
