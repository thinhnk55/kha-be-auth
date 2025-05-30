package com.defi.auth.group.service;

import com.defi.auth.user.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserInGroupService {
    List<User> findUsersByGroupId(Long groupId, Pageable pageable);

    void addUsersToGroup(List<Long> userId, Long groupId);

    void removeUserFromGroup(Long userId, Long groupId);

    List<Long> findGroupIdsByUserId(Long userId);
}
