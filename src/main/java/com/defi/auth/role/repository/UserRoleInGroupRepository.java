package com.defi.auth.role.repository;

import com.defi.auth.role.entity.UserRoleInGroup;
import com.defi.auth.role.entity.UserRoleInGroupId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleInGroupRepository extends JpaRepository<UserRoleInGroup, UserRoleInGroupId> {
    List<UserRoleInGroup> findAllByIdUserId(Long userId);
    List<UserRoleInGroup> findAllByIdGroupId(Long groupId);
    List<UserRoleInGroup> findAllByIdRoleId(Long roleId);
    List<UserRoleInGroup> findAllByIdUserIdAndIdGroupId(Long userId, Long groupId);
}