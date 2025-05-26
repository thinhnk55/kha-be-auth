package com.defi.auth.group.repository;

import com.defi.auth.group.entity.UserInGroup;
import com.defi.auth.group.entity.UserInGroupId;
import com.defi.auth.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserInGroupRepository extends JpaRepository<UserInGroup, UserInGroupId> {
    @Query("""
        SELECT u FROM User u
        JOIN UserInGroup ug ON u.id = ug.id.userId
        WHERE ug.id.groupId = :groupId
    """)
    List<User> findUsersByGroupId(@Param("groupId") Long groupId, Pageable pageable);

    @Query("""
        SELECT ug.id.groupId FROM UserInGroup ug
        WHERE ug.id.userId = :userId
    """)
    List<Long> findGroupIdsByUserId(@Param("userId") Long userId);
}
