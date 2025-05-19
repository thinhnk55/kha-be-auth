package com.defi.auth.user.service.impl;

import com.defi.common.CommonMessage;
import com.defi.auth.user.dto.AdminCreateUserRequest;
import com.defi.auth.user.dto.UpdateUserRequest;
import com.defi.auth.user.entity.User;
import com.defi.auth.user.entity.UserCredential;
import com.defi.auth.user.entity.UserCredentialId;
import com.defi.auth.user.mapper.UserMapper;
import com.defi.auth.user.repository.UserCredentialRepository;
import com.defi.auth.user.repository.UserRepository;
import com.defi.auth.user.service.AdminUserService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;
    private final UserCredentialRepository credentialRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public List<User> listUsers(Pageable pageable) {
        return userRepository.findAll(pageable).getContent();
    }

    @Override
    @Transactional
    public User createUser(AdminCreateUserRequest request) {
        if (userRepository.existsByUserName(request.getUserName())
        || userRepository.existsByEmail(request.getEmail())
        || userRepository.existsByPhone(request.getPhone())) {
            throw new ResponseStatusException(CONFLICT, CommonMessage.EXISTING);
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
    public User updateUser(Long id, UpdateUserRequest request) {
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
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, CommonMessage.NOT_FOUND));
        credential.setSecretData(passwordEncoder.encode(newPassword));
        credentialRepository.save(credential);
    }

    public void updateMetadata(Long id, ObjectNode metadata) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, CommonMessage.NOT_FOUND));
        user.setMetadata(metadata);
        userRepository.save(user);
    }

    @Override
    public void lockUser(Long userId, boolean isLocked, long lockedUntil) {
        User user = getUser(userId);

        if (isLocked) {
            user.setLocked(true);
            user.setLockedUntil(lockedUntil);
        } else {
            user.setLocked(false);
            user.setLockedUntil(null);
        }
        userRepository.save(user);
    }
}
