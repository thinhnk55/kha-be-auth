package com.defi.auth.role.service;

import com.defi.auth.role.entity.UserRoleInGroup;

import java.util.List;

public interface UserRoleInGroupService {
    void addUserRoleInGroup(Long userId, Long roleId, Long groupId);
    void removeUserRoleInGroup(Long userId, Long roleId, Long groupId);
    List<UserRoleInGroup> getUserRolesInGroup(Long userId, Long groupId);
    List<UserRoleInGroup> findAllByIdUserId(Long userId);
    List<UserRoleInGroup> findAllByIdGroupId(Long userId);
    List<UserRoleInGroup> findAllByIdRoleId(Long groupId);

    List<UserRoleInGroup> findAll();
}
