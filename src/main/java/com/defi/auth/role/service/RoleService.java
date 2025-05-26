package com.defi.auth.role.service;

import com.defi.auth.role.entity.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    Role createRole(Role role);
    Optional<Role> findById(Long id);
    Optional<Role> findByCode(String code);
    List<Role> listRoles();
    Role updateRole(Long id, Role role);
    void deleteRole(Long id);
}
