package com.defi.auth.permission.controler;

import com.defi.auth.permission.dto.PermissionDto;
import com.defi.auth.permission.dto.PermissionRequest;
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
    public ResponseEntity<BaseResponse<List<PermissionDto>>> getAll() {
        return ResponseEntity.ok(BaseResponse.of(permissionService.findAll()));
    }


    @GetMapping("/by-role/{roleId}")
    public ResponseEntity<BaseResponse<List<PermissionDto>>> findByRoleId(
            @PathVariable Long roleId
    ) {
        List<PermissionDto> result = permissionService.findByRoleId(roleId);
        return ResponseEntity.ok(BaseResponse.of(result));
    }

    @GetMapping("/by-resource/{resourceId}")
    public ResponseEntity<BaseResponse<List<PermissionDto>>> findByResourceId(
            @PathVariable Long resourceId
    ) {
        List<PermissionDto> result = permissionService.findByResourceId(resourceId);
        return ResponseEntity.ok(BaseResponse.of(result));
    }

    @PostMapping
    public ResponseEntity<BaseResponse<PermissionDto>> create(@RequestBody @Valid PermissionRequest request) {
        PermissionDto permission = permissionService.create(request);
        return ResponseEntity.ok(BaseResponse.of(permission));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<?>> delete(@PathVariable Long id) {
        permissionService.delete(id);
        return ResponseEntity.ok(BaseResponse.of("deleted"));
    }
}
