package com.defi.auth.permission.service.impl;

import com.defi.auth.permission.dto.PermissionRequest;
import com.defi.auth.permission.entity.Permission;
import com.defi.auth.permission.mapper.PermissionMapper;
import com.defi.auth.permission.repository.PermissionRepository;
import com.defi.auth.permission.service.PermissionService;
import com.defi.common.api.CommonMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    @Override
    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    @Override
    public Permission findById(Long id) {
        return permissionRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, CommonMessage.NOT_FOUND)
                );
    }

    @Override
    @Transactional
    public Permission create(PermissionRequest request) {
        if (permissionRepository.existsByRoleIdAndResourceIdAndActionId(
                request.getRoleId(), request.getResourceId(), request.getActionId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, CommonMessage.EXISTING);
        }
        Permission permission = permissionMapper.toEntity(request);
        permissionRepository.save(permission);
        return permission;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!permissionRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, CommonMessage.NOT_FOUND);
        }
        permissionRepository.deleteById(id);
    }

    @Override
    public List<Permission> findByRoleCode(Long roleId) {
        return permissionRepository.findByRoleId(roleId);
    }
    @Override
    public List<Permission> findByResourceCode(Long resourceId) {
        return permissionRepository.findByResourceId(resourceId);
    }
}
