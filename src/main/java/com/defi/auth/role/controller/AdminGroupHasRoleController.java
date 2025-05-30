package com.defi.auth.role.controller;

import com.defi.auth.role.dto.GroupHasRoleDto;
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

    @PostMapping
    public ResponseEntity<BaseResponse<?>> assignRoleToGroup(
            @RequestBody GroupHasRoleDto request) {
        groupHasRoleService.assignRoleToGroup(request.getGroupId(), request.getRoleId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<BaseResponse<?>> removeRoleFromGroup(
            @RequestBody GroupHasRoleDto request) {
        groupHasRoleService.removeRoleFromGroup(request.getGroupId(), request.getRoleId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/by-group")
    public ResponseEntity<BaseResponse<List<GroupHasRole>>> findAllByIdGroupId(
            @RequestParam Long groupId) {
        List<GroupHasRole> list = groupHasRoleService.findAllByIdGroupId(groupId);
        return ResponseEntity.ok(BaseResponse.of(list));
    }

    @GetMapping("/by-role")
    public ResponseEntity<BaseResponse<List<GroupHasRole>>> findAllByIdRoleId(
            @RequestParam Long roleId) {
        List<GroupHasRole> list = groupHasRoleService.findAllByIdRoleId(roleId);
        return ResponseEntity.ok(BaseResponse.of(list));
    }

    @GetMapping("/role-ids")
    public ResponseEntity<BaseResponse<List<Long>>> findRoleIdsByGroupId(
            @RequestParam Long groupId) {
        List<Long> roleIds = groupHasRoleService.findRoleIdsByGroupId(groupId);
        return ResponseEntity.ok(BaseResponse.of(roleIds));
    }
}
