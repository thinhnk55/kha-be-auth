package com.defi.auth.role.repository;

import com.defi.auth.role.entity.UserHasRole;
import com.defi.auth.role.entity.UserHasRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserHasRoleRepository extends JpaRepository<UserHasRole, UserHasRoleId> {
    List<UserHasRole> findAllByIdUserId(Long userId);
    List<UserHasRole> findAllByIdRoleId(Long roleId);
}