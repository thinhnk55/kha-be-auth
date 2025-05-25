package com.defi.auth.permission.service.impl;

import com.defi.auth.permission.dto.PermissionDto;
import com.defi.auth.permission.dto.PermissionRequest;
import com.defi.auth.permission.entity.Permission;
import com.defi.auth.permission.mapper.PermissionMapper;
import com.defi.auth.permission.repository.PermissionRepository;
import com.defi.auth.permission.service.PermissionService;
import com.defi.common.CommonMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    @Override
    public List<PermissionDto> findAll() {
        return permissionRepository.findAll().stream()
                .map(permissionMapper::toDto)
                .toList();
    }

    @Override
    public Optional<PermissionDto> findById(Long id) {
        return permissionRepository.findById(id).map(permissionMapper::toDto);
    }

    @Override
    @Transactional
    public PermissionDto create(PermissionRequest request) {
        if (permissionRepository.existsByRoleIdAndResourceIdAndActionId(
                request.getRoleId(), request.getResourceId(), request.getActionId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, CommonMessage.EXISTING);
        }
        Permission permission = permissionMapper.toEntity(request);
        permissionRepository.save(permission);
        return permissionMapper.toDto(permission);
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
    public List<PermissionDto> findByRoleId(Long roleId) {
        return permissionRepository.findByRoleId(roleId)
                .stream().map(permissionMapper::toDto).toList();
    }
    @Override
    public List<PermissionDto> findByResourceId(Long resourceId) {
        return permissionRepository.findByResourceId(resourceId)
                .stream().map(permissionMapper::toDto).toList();
    }
}
