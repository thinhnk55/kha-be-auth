package com.defi.auth.role.service;

import com.defi.auth.role.entity.GroupHasRole;

import java.util.List;

public interface GroupHasRoleService {
    void assignRoleListToGroup(Long groupId, List<Long> roleList);

    void removeRoleFromGroup(Long groupId, Long roleId);

    List<GroupHasRole> findAllByIdGroupId(Long groupId);

    List<GroupHasRole> findAllByIdRoleId(Long roleId);

    GroupHasRole getGroupHasRole(Long groupId, Long roleId);

    List<Long> findRoleIdsByGroupId(Long groupId);

    List<Long> findGroupIdsByRoleId(Long roleId);
}