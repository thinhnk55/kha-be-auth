package com.defi.auth.permission.service;

import com.defi.auth.permission.dto.PermissionRequest;
import com.defi.auth.permission.entity.Permission;

import java.util.List;

public interface PermissionService {
    List<Permission> findAll();
    Permission findById(Long id);
    Permission create(PermissionRequest request);
    void delete(Long id);
    List<Permission> findByRoleCode(Long roleId);
    List<Permission> findByResourceCode(Long resourceId);
}
