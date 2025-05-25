package com.defi.auth.role.service;

import com.defi.auth.role.entity.UserHasRole;

import java.util.List;

public interface UserHasRoleService {
    void assignRoleToUser(Long userId, Long roleId);
    void removeRoleFromUser(Long userId, Long roleId);

    List<UserHasRole> getUserHasRole(Long userId, Long groupId);

    List<UserHasRole> findAllByIdUserId(Long userId);
    List<UserHasRole> findAllByIdRoleId(Long roleId);
}
