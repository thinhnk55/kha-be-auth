package com.defi.auth.permission.repository;

import com.defi.auth.permission.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    
    @Query("SELECT p FROM Permission p WHERE p.roleId = ?1")
    List<Permission> findByRoleId(Long roleId);

    @Query("SELECT p FROM Permission p WHERE p.resourceId = ?1")
    List<Permission> findByResourceId(Long resourceId);

    @Modifying
    @Query("DELETE FROM Permission p WHERE p.roleId = ?1")
    void deleteByRoleId(Long roleId);
}
