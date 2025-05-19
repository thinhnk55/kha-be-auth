package com.defi.auth.permission.controler;

import com.defi.auth.permission.dto.PermissionDto;
import com.defi.auth.permission.dto.PermissionRequest;
import com.defi.auth.permission.service.PermissionService;
import com.defi.common.BaseResponse;
import com.defi.common.CommonMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/auth/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping
    public ResponseEntity<BaseResponse<List<PermissionDto>>> getAll() {
        return ResponseEntity.ok(BaseResponse.of(permissionService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<PermissionDto>> getById(@PathVariable Long id) {
        return permissionService.findById(id)
                .map(permission -> ResponseEntity.ok(BaseResponse.of(permission)))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, CommonMessage.NOT_FOUND));
    }

    @GetMapping("/by-role-group")
    public ResponseEntity<BaseResponse<List<PermissionDto>>> findByRoleAndGroup(
            @RequestParam(required = false) Long roleId,
            @RequestParam(required = false) Long groupId
    ) {
        List<PermissionDto> result = permissionService.findByRoleAndGroup(roleId, groupId);
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
