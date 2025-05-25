package com.defi.auth.permission.repository;

import com.defi.auth.permission.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByRoleIdAndAndResourceIdAndActionId(Long roleId, Long resourceId, Long actionId);
    boolean existsByRoleIdAndResourceIdAndActionId(Long roleId, Long resourceId, Long actionId);
    List<Permission> findByRoleId(Long roleId);
    List<Permission> findByResourceId(Long resourceId);
}
