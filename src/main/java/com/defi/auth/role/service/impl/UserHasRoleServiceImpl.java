package com.defi.auth.role.service.impl;

import com.defi.auth.role.entity.UserHasRole;
import com.defi.auth.role.entity.UserHasRoleId;
import com.defi.auth.role.repository.UserHasRoleRepository;
import com.defi.auth.role.service.UserHasRoleService;
import com.defi.auth.user.repository.UserEffectiveRoleViewRepository;
import com.defi.common.CommonMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserHasRoleServiceImpl implements UserHasRoleService {

    private final UserHasRoleRepository userHasRoleRepository;
    private final UserEffectiveRoleViewRepository effectiveRoleViewRepository;

    @Override
    public List<Long> findRoleIdsByUserId(Long userId) {
        return effectiveRoleViewRepository.findRoleIdsByUserId(userId);
    }

    @Override
    public void assignRoleToUser(Long userId, Long roleId) {
        UserHasRoleId id = new UserHasRoleId(userId, roleId);
        if (userHasRoleRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, CommonMessage.EXISTING);
        }
        UserHasRole entity = UserHasRole.builder()
                .id(id)
                .build();
        userHasRoleRepository.save(entity);
    }

    @Override
    public void removeRoleFromUser(Long userId, Long roleId) {
        UserHasRoleId id = new UserHasRoleId(userId, roleId);
        userHasRoleRepository.deleteById(id);
    }

    @Override
    public List<UserHasRole> findAllByIdUserId(Long userId) {
        return userHasRoleRepository.findAllByIdUserId(userId);
    }

    @Override
    public List<UserHasRole> findAllByIdRoleId(Long roleId) {
        return userHasRoleRepository.findAllByIdRoleId(roleId);
    }

    @Override
    public UserHasRole getUserHasRole(Long userId, Long roleId) {
        UserHasRoleId id = UserHasRoleId.builder()
                .roleId(roleId)
                .userId(userId)
                .build();
        return userHasRoleRepository.findById(id).orElseThrow(
                ()->new ResponseStatusException(HttpStatus.NOT_FOUND, CommonMessage.NOT_FOUND)
        );
    }
}
