package com.defi.auth.role.controller;

import com.defi.auth.role.entity.Role;
import com.defi.auth.role.service.RoleService;
import com.defi.common.BaseResponse;
import com.defi.common.CommonMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/auth/v1/admin/roles")
@RequiredArgsConstructor
public class AdminRoleController {

    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<BaseResponse<List<Role>>> listRoles() {
        List<Role> roles = roleService.listRoles();
        return ResponseEntity.ok(BaseResponse.of(roles));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<Role>> getRole(@PathVariable Long id) {
        Role role = roleService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, CommonMessage.NOT_FOUND));
        return ResponseEntity.ok(BaseResponse.of(role));
    }

    @PostMapping
    public ResponseEntity<BaseResponse<Role>> createRole(@RequestBody Role role) {
        Role saved = roleService.createRole(role);
        return ResponseEntity.ok(BaseResponse.of(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<Role>> updateRole(
            @PathVariable Long id,
            @RequestBody Role update
    ) {
        Role saved = roleService.updateRole(id, update);
        return ResponseEntity.ok(BaseResponse.of(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.ok().build();
    }
}
