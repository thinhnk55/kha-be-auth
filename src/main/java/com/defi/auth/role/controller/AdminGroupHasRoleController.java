package com.defi.auth.role.controller;

import com.defi.auth.role.entity.GroupHasRole;
import com.defi.auth.role.service.GroupHasRoleService;
import com.defi.common.api.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth/v1/admin/group-has-role")
@RequiredArgsConstructor
public class AdminGroupHasRoleController {

    private final GroupHasRoleService groupHasRoleService;

    @GetMapping("/{groupId}/{roleId}")
    public ResponseEntity<BaseResponse<GroupHasRole>> getGroupHasRole(
            @PathVariable Long groupId,
            @PathVariable Long roleId) {
        GroupHasRole groupHasRole = groupHasRoleService.getGroupHasRole(groupId, roleId);
        return ResponseEntity.ok(BaseResponse.of(groupHasRole));
    }

    @PostMapping("/{groupId}/roles")
    public ResponseEntity<BaseResponse<?>> assignRoleToGroup(
            @PathVariable Long groupId,
            @RequestBody List<Long> roleList) {
        groupHasRoleService.assignRoleListToGroup(groupId, roleList);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{groupId}/{roleId}")
    public ResponseEntity<BaseResponse<?>> removeRoleFromGroup(
            @PathVariable Long groupId,
            @PathVariable Long roleId) {
        groupHasRoleService.removeRoleFromGroup(groupId, roleId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/groups/{groupId}")
    public ResponseEntity<BaseResponse<List<Long>>> findRoleIdsByGroupId(
            @PathVariable Long groupId) {
        List<Long> roleIds = groupHasRoleService.findRoleIdsByGroupId(groupId);
        return ResponseEntity.ok(BaseResponse.of(roleIds));
    }

    @GetMapping("/roles/{roleId}")
    public ResponseEntity<BaseResponse<List<Long>>> findGroupIdsByRoleId(
            @PathVariable Long roleId) {
        List<Long> groupIds = groupHasRoleService.findGroupIdsByRoleId(roleId);
        return ResponseEntity.ok(BaseResponse.of(groupIds));
    }
}
