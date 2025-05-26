package com.defi.auth.user.repository;


import com.defi.auth.user.entity.UserEffectiveRoleView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserEffectiveRoleViewRepository extends JpaRepository<UserEffectiveRoleView, UserEffectiveRoleView.PK> {
    @Query("SELECT u.roleId FROM UserEffectiveRoleView u WHERE u.userId = :userId")
    List<Long> findRoleIdsByUserId(Long userId);
}