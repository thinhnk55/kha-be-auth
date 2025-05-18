package com.defi.auth.user.mapper;

import com.defi.auth.user.dto.AdminCreateUserRequest;
import com.defi.auth.user.dto.AdminUpdateUserRequest;
import com.defi.auth.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toUser(AdminCreateUserRequest req) {
        return User.builder()
                .userName(req.getUserName())
                .fullName(req.getFullName())
                .email(req.getEmail())
                .emailVerified(false)
                .phone(null)
                .phoneVerified(false)
                .locked(false)
                .lockedUntil(null)
                .build();
    }

    public void updateUser(User user, AdminUpdateUserRequest req) {
        user.setFullName(req.getFullName());
        user.setEmail(req.getEmail());
        user.setPhone(req.getPhone());
    }
}
