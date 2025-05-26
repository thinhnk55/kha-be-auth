package com.defi.auth.user.service;

import com.defi.auth.user.dto.CreateUserRequest;
import com.defi.auth.user.dto.UpdateUserRequest;
import com.defi.auth.user.entity.User;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminUserService {
    List<User> listUsers(Pageable pageable);
    User createUser(CreateUserRequest request);
    User getUser(Long id);
    User updateUser(Long id, UpdateUserRequest request);
    void deleteUser(Long id);
    void updatePassword(Long userId, String newPassword);
    void updateMetadata(Long id, ObjectNode metadata);
    void lockUser(Long userId, boolean isLocked, long lockedUntil);
}

