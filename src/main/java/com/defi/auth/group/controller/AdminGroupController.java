package com.defi.auth.group.controller;

import com.defi.auth.group.dto.CreateGroupRequest;
import com.defi.auth.group.dto.UpdateGroupMetadataRequest;
import com.defi.auth.group.dto.UpdateGroupRequest;
import com.defi.auth.group.entity.Group;
import com.defi.auth.group.service.GroupService;
import com.defi.auth.group.service.UserInGroupService;
import com.defi.auth.user.entity.User;
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
@RequestMapping("/auth/v1/admin/groups")
@RequiredArgsConstructor
public class AdminGroupController {

    private final GroupService groupService;
    private final UserInGroupService userInGroupService;

    @GetMapping()
    public ResponseEntity<BaseResponse<List<Group>>> findAll() {
        List<Group> list = groupService.findAll();
        return ResponseEntity.ok(BaseResponse.of(list));
    }


    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<Group>> get(@PathVariable Long id) {
        return ResponseEntity.ok(BaseResponse.of(groupService.getGroup(id)));
    }


    @PostMapping
    public ResponseEntity<BaseResponse<Group>> create(@Valid @RequestBody CreateGroupRequest req) {
        Group group = groupService.createGroup(req);
        return ResponseEntity.ok(BaseResponse.of(group));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<Group>> update(@PathVariable Long id, @Valid @RequestBody UpdateGroupRequest req) {
        Group group = groupService.updateGroup(id, req);
        return ResponseEntity.ok(BaseResponse.of(group));
    }

    @PutMapping("/{id}/metadata")
    public ResponseEntity<BaseResponse<Void>> updateMetadata(@PathVariable Long id,
                                                             @RequestBody UpdateGroupMetadataRequest req) {
        groupService.updateMetadata(id, req);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> delete(@PathVariable Long id) {
        groupService.deleteGroup(id);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{groupId}/users/{userId}")
    public ResponseEntity<BaseResponse<List<User>>> removeUserFromGroup(
            @PathVariable Long groupId,
            @PathVariable Long userId
    ) {
        userInGroupService.removeUserFromGroup(userId, groupId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{groupId}/users")
    public ResponseEntity<BaseResponse<List<User>>> getUsersByGroup(
            @PathVariable Long groupId,
            @PageableDefault Pageable pageable
    ) {
        List<User> users = userInGroupService.findUsersByGroupId(groupId, pageable);
        Pagination pagination = Pagination.of(pageable);
        return ResponseEntity.ok(BaseResponse.of(users, pagination));
    }

    @PostMapping("/{groupId}/users")
    public ResponseEntity<BaseResponse<List<User>>> addUsersToGroup(
            @PathVariable Long groupId,
            @RequestBody List<Long> userIds
    ) {
        userInGroupService.addUsersToGroup(userIds, groupId);
        return ResponseEntity.ok().build();
    }
}
