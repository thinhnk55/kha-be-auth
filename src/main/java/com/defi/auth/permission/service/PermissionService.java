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
    List<PermissionDto> findByRoleId(Long roleId);

    List<PermissionDto> findByResourceId(Long resourceId);
}
