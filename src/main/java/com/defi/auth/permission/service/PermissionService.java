package com.defi.auth.permission.service;

import com.defi.auth.permission.dto.PermissionDto;
import com.defi.auth.permission.dto.PermissionRequest;

import java.util.List;
import java.util.Optional;

public interface PermissionService {
    List<PermissionDto> findAll();
    Optional<PermissionDto> findById(Long id);
    PermissionDto create(PermissionRequest request);
    void delete(Long id);
    Optional<PermissionDto> findByUnique(Long roleId, Long groupId, Long resourceId, Long actionId);

    List<PermissionDto> findByRoleAndGroup(Long roleId, Long groupId);
}
