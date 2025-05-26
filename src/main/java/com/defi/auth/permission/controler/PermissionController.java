package com.defi.auth.permission.controler;

import com.defi.auth.permission.dto.PermissionRequest;
import com.defi.auth.permission.entity.Permission;
import com.defi.auth.permission.service.PermissionService;
import com.defi.common.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth/v1/admin/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping
    public ResponseEntity<BaseResponse<List<Permission>>> getAll() {
        return ResponseEntity.ok(BaseResponse.of(permissionService.findAll()));
    }


    @GetMapping("/by-role/{roleId}")
    public ResponseEntity<BaseResponse<List<Permission>>> findByRoleId(
            @PathVariable Long roleId
    ) {
        List<Permission> result = permissionService.findByRoleCode(roleId);
        return ResponseEntity.ok(BaseResponse.of(result));
    }

    @GetMapping("/by-resource/{resourceId}")
    public ResponseEntity<BaseResponse<List<Permission>>> findByResourceCode(
            @PathVariable Long resourceId
    ) {
        List<Permission> result = permissionService.findByResourceCode(resourceId);
        return ResponseEntity.ok(BaseResponse.of(result));
    }

    @PostMapping
    public ResponseEntity<BaseResponse<Permission>> create(@RequestBody @Valid PermissionRequest request) {
        Permission permission = permissionService.create(request);
        return ResponseEntity.ok(BaseResponse.of(permission));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<?>> delete(@PathVariable Long id) {
        permissionService.delete(id);
        return ResponseEntity.ok().build();
    }
}
