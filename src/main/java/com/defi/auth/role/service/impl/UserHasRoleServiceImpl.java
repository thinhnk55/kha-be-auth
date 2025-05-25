package com.defi.auth.role.service.impl;

import com.defi.auth.role.entity.UserHasRole;
import com.defi.auth.role.entity.UserHasRoleId;
import com.defi.auth.role.repository.UserRoleInGroupRepository;
import com.defi.auth.role.service.UserHasRoleService;
import com.defi.common.CommonMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserHasRoleServiceImpl implements UserHasRoleService {

    private final UserRoleInGroupRepository userRoleInGroupRepository;

    @Override
    public void assignRoleToUser(Long userId, Long roleId) {
        UserHasRoleId id = new UserHasRoleId(userId, roleId);
        if (userRoleInGroupRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, CommonMessage.EXISTING);
        }
        UserHasRole entity = UserHasRole.builder()
                .id(id)
                .assignedAt(System.currentTimeMillis())
                .build();
        userRoleInGroupRepository.save(entity);
    }

    @Override
    public void removeRoleFromUser(Long userId, Long roleId) {
        UserHasRoleId id = new UserHasRoleId(userId, roleId);
        userRoleInGroupRepository.deleteById(id);
    }

    @Override
    public List<UserHasRole> getUserHasRole(Long userId, Long groupId) {
        return userRoleInGroupRepository.findAllByIdUserIdAndIdGroupId(userId, groupId);
    }

    @Override
    public List<UserHasRole> findAllByIdUserId(Long userId) {
        return userRoleInGroupRepository.findAllByIdUserId(userId);
    }

    @Override
    public List<UserHasRole> findAllByIdRoleId(Long roleId) {
        return userRoleInGroupRepository.findAllByIdRoleId(roleId);
    }
}
