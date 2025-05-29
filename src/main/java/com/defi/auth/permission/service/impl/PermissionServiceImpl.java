package com.defi.auth.permission.service.impl;

import com.defi.auth.permission.dto.UpdateRolePermissionsRequest;
import com.defi.auth.permission.entity.Permission;
import com.defi.auth.permission.repository.PermissionRepository;
import com.defi.auth.permission.service.PermissionService;
import com.defi.common.api.CommonMessage;
import com.defi.common.casbin.event.PolicyEventPublisher;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final PolicyEventPublisher policyEventPublisher;

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
    public List<Permission> findByRoleId(Long roleId) {
        return permissionRepository.findByRoleId(roleId);
    }
    
    @Override
    public List<Permission> findByResourceId(Long resourceId) {
        return permissionRepository.findByResourceId(resourceId);
    }

    @Override
    @Transactional
    public Integer updateRolePermissions(UpdateRolePermissionsRequest request) {
        log.info("Updating permissions for role: {}", request.getRoleId());

        // Delete existing permissions for the role
        permissionRepository.deleteByRoleId(request.getRoleId());

        // Create new permissions
        List<Permission> newPermissions = new ArrayList<>();
        for (UpdateRolePermissionsRequest.ResourcePermission rp :
         request.getResourcePermissions()) {
            for (Long actionId : rp.getActionIds()) {
                Permission permission = Permission.builder()
                        .roleId(request.getRoleId())
                        .resourceId(rp.getResourceId())
                        .actionId(actionId)
                        .build();
                newPermissions.add(permission);
            }
        }

        permissionRepository.saveAll(newPermissions);
        log.info("Created {} new permissions for role: {}",
         newPermissions.size(), request.getRoleId());

        // Publish event to reload policies
        policyEventPublisher.publishReloadEvent();
        
        return newPermissions.size();
    }
}
