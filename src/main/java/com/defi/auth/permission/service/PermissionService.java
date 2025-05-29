package com.defi.auth.permission.service;

import com.defi.auth.permission.dto.UpdateRolePermissionsRequest;
import com.defi.auth.permission.entity.Permission;

import java.util.List;

public interface PermissionService {
    List<Permission> findAll();
    Permission findById(Long id);
    List<Permission> findByRoleId(Long roleId);
    List<Permission> findByResourceId(Long resourceId);
    Integer updateRolePermissions(UpdateRolePermissionsRequest request);
}
