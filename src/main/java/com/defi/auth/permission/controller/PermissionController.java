package com.defi.auth.permission.controller;

import com.defi.auth.permission.dto.UpdateRolePermissionsRequest;
import com.defi.auth.permission.dto.UpdateRolePermissionsResponse;
import com.defi.auth.permission.entity.Permission;
import com.defi.auth.permission.service.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/permissions")
@RequiredArgsConstructor
@Slf4j
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping
    public ResponseEntity<List<Permission>> getAllPermissions() {
        return ResponseEntity.ok(permissionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Permission> getPermissionById(@PathVariable Long id) {
        return ResponseEntity.ok(permissionService.findById(id));
    }

    @GetMapping("/role/{roleId}")
    public ResponseEntity<List<Permission>> getPermissionsByRoleId(@PathVariable Long roleId) {
        return ResponseEntity.ok(permissionService.findByRoleId(roleId));
    }

    @GetMapping("/resource/{resourceId}")
    public ResponseEntity<List<Permission>> getPermissionsByResourceId(@PathVariable Long resourceId) {
        return ResponseEntity.ok(permissionService.findByResourceId(resourceId));
    }

    @PutMapping("/role")
    public ResponseEntity<UpdateRolePermissionsResponse> updateRolePermissions(
            @Valid @RequestBody UpdateRolePermissionsRequest request) {
        Integer totalPermissions = permissionService.updateRolePermissions(request);
        UpdateRolePermissionsResponse response = UpdateRolePermissionsResponse.success(
                request.getRoleId(), totalPermissions);
        return ResponseEntity.ok(response);
    }
} 