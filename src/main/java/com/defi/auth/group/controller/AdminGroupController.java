package com.defi.auth.group.controller;

import com.defi.auth.group.dto.CreateGroupRequest;
import com.defi.auth.group.dto.GroupDTO;
import com.defi.auth.group.dto.UpdateGroupMetadataRequest;
import com.defi.auth.group.dto.UpdateGroupRequest;
import com.defi.auth.group.service.GroupService;
import com.defi.auth.user.entity.User;
import com.defi.common.BaseResponse;
import com.defi.common.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth/admin/groups")
@RequiredArgsConstructor
public class AdminGroupController {

    private final GroupService groupService;

    @PostMapping
    public ResponseEntity<BaseResponse<GroupDTO>> create(@Valid @RequestBody CreateGroupRequest req) {
        GroupDTO group = groupService.createGroup(req);
        return ResponseEntity.ok(BaseResponse.of(group));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<GroupDTO>> get(@PathVariable Long id) {
        return ResponseEntity.ok(BaseResponse.of(groupService.getGroup(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<GroupDTO>> update(@PathVariable Long id, @Valid @RequestBody UpdateGroupRequest req) {
        GroupDTO group = groupService.updateGroup(id, req);
        return ResponseEntity.ok(BaseResponse.of(group));
    }

    @PutMapping("/{id}/metadata")
    public ResponseEntity<BaseResponse<Void>> updateMetadata(@PathVariable Long id, @RequestBody UpdateGroupMetadataRequest req) {
        groupService.updateMetadata(id, req);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> delete(@PathVariable Long id) {
        groupService.deleteGroup(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<GroupDTO>>> findAll() {
        List<GroupDTO> groups = groupService.findAll();
        return ResponseEntity.ok(BaseResponse.of(groups));
    }

    @GetMapping("/{groupId}/users")
    public ResponseEntity<BaseResponse<List<User>>> getUsersByGroup(
            @PathVariable Long groupId,
            @PageableDefault Pageable pageable
    ) {
        List<User> users = groupService.getUsersByGroup(groupId, pageable);
        Pagination pagination = Pagination.of(pageable);
        return ResponseEntity.ok(BaseResponse.of(users, pagination));
    }

    @PostMapping("/{groupId}/users/{userId}")
    public ResponseEntity<BaseResponse<Void>> addUserToGroup(
            @PathVariable Long groupId,
            @PathVariable Long userId
    ) {
        groupService.addUserToGroup(userId, groupId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{groupId}/users/{userId}")
    public ResponseEntity<BaseResponse<Void>> removeUserFromGroup(
            @PathVariable Long groupId,
            @PathVariable Long userId
    ) {
        groupService.removeUserFromGroup(userId, groupId);
        return ResponseEntity.ok().build();
    }
}
