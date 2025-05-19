package com.defi.auth.role.service.impl;

import com.defi.auth.role.entity.UserRoleInGroup;
import com.defi.auth.role.entity.UserRoleInGroupId;
import com.defi.auth.role.repository.UserRoleInGroupRepository;
import com.defi.auth.role.service.UserRoleInGroupService;
import com.defi.common.CommonMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRoleInGroupServiceImpl implements UserRoleInGroupService {

    private final UserRoleInGroupRepository userRoleInGroupRepository;

    @Override
    public void addUserRoleInGroup(Long userId, Long roleId, Long groupId) {
        UserRoleInGroupId id = new UserRoleInGroupId(userId, roleId, groupId);
        if (userRoleInGroupRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, CommonMessage.EXISTING);
        }
        UserRoleInGroup entity = UserRoleInGroup.builder()
                .id(id)
                .assignedAt(System.currentTimeMillis())
                .build();
        userRoleInGroupRepository.save(entity);
    }

    @Override
    public void removeUserRoleInGroup(Long userId, Long roleId, Long groupId) {
        UserRoleInGroupId id = new UserRoleInGroupId(userId, roleId, groupId);
        userRoleInGroupRepository.deleteById(id);
    }

    @Override
    public List<UserRoleInGroup> getUserRolesInGroup(Long userId, Long groupId) {
        return userRoleInGroupRepository.findAllByIdUserIdAndIdGroupId(userId, groupId);
    }

    @Override
    public List<UserRoleInGroup> findAllByIdUserId(Long userId) {
        return userRoleInGroupRepository.findAllByIdUserId(userId);
    }

    @Override
    public List<UserRoleInGroup> findAllByIdGroupId(Long groupId) {
        return userRoleInGroupRepository.findAllByIdGroupId(groupId);
    }
    @Override
    public List<UserRoleInGroup> findAllByIdRoleId(Long roleId) {
        return userRoleInGroupRepository.findAllByIdRoleId(roleId);
    }
    @Override
    public List<UserRoleInGroup> findAll() {
        return userRoleInGroupRepository.findAll();
    }
}
