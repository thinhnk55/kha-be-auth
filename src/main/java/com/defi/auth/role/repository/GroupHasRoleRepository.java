package com.defi.auth.role.repository;

import com.defi.auth.role.entity.GroupHasRole;
import com.defi.auth.role.entity.GroupHasRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupHasRoleRepository extends JpaRepository<GroupHasRole, GroupHasRoleId> {
    List<GroupHasRole> findAllByIdGroupId(Long groupId);

    List<GroupHasRole> findAllByIdRoleId(Long roleId);
}