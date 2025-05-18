package com.defi.auth.user.service.impl;

import com.defi.auth.user.dto.AdminCreateUserRequest;
import com.defi.auth.user.dto.AdminUpdateUserRequest;
import com.defi.auth.user.entity.User;
import com.defi.auth.user.entity.UserCredential;
import com.defi.auth.user.entity.UserCredentialId;
import com.defi.auth.user.mapper.UserMapper;
import com.defi.auth.user.repository.UserCredentialRepository;
import com.defi.auth.user.repository.UserRepository;
import com.defi.auth.user.service.AdminUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;
    private final UserCredentialRepository credentialRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public User createUser(AdminCreateUserRequest request) {
        if (userRepository.existsByUserName(request.getUserName())) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = userMapper.toUser(request);
        userRepository.save(user);

        String salt = UUID.randomUUID().toString().replace("-", "")
                .substring(0, 16);
        String hash = passwordEncoder.encode(salt + request.getPassword());

        UserCredential credential = UserCredential.builder()
                .id(new UserCredentialId(user.getId(), "password"))
                .salt(salt)
                .secretData(hash)
                .build();

        credentialRepository.save(credential);
        return user;
    }


    @Override
    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND
                ));
    }

    @Override
    public User updateUser(Long id, AdminUpdateUserRequest request) {
        User user = getUser(id);
        userMapper.updateUser(user, request);
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(
                    NOT_FOUND
            );
        }
        userRepository.deleteById(id);
    }

    @Override
    public void updatePassword(Long userId, String newPassword) {
        UserCredentialId id = new UserCredentialId(userId, "password");
        UserCredential credential = credentialRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Password credential not found"));

        credential.setSecretData(passwordEncoder.encode(newPassword));
        credentialRepository.save(credential);
    }

    @Override
    public void lockUser(Long userId, boolean isLocked, long lockedUntil) {
        User user = getUser(userId);

        if (isLocked) {
            user.setLocked(true);
            user.setLockedUntil(lockedUntil == 0 ? 0 : lockedUntil);
        } else {
            user.setLocked(false);
            user.setLockedUntil(null);
        }

        userRepository.save(user);
    }
}
