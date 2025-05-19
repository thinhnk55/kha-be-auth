package com.defi.auth.permission.repository;

import com.defi.auth.permission.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByRoleIdAndGroupIdAndResourceIdAndActionId(Long roleId, Long groupId, Long resourceId, Long actionId);
    boolean existsByRoleIdAndGroupIdAndResourceIdAndActionId(Long roleId, Long groupId, Long resourceId, Long actionId);
    List<Permission> findByRoleIdAndGroupId(Long roleId, Long groupId);
    List<Permission> findByResourceId(Long resourceId);
}
