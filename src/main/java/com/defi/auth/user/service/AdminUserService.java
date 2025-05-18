package com.defi.auth.user.service;

import com.defi.auth.user.dto.AdminCreateUserRequest;
import com.defi.auth.user.dto.AdminUpdateUserRequest;
import com.defi.auth.user.entity.User;

public interface AdminUserService {
    User createUser(AdminCreateUserRequest request);
    User getUser(Long id);
    User updateUser(Long id, AdminUpdateUserRequest request);
    void deleteUser(Long id);
    void updatePassword(Long userId, String newPassword);
    void lockUser(Long userId, boolean isLocked, long lockedUntil);
}

