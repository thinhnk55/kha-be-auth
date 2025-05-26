package com.defi.auth.group.service.impl;

import com.defi.auth.group.entity.UserInGroup;
import com.defi.auth.group.entity.UserInGroupId;
import com.defi.auth.group.repository.UserInGroupRepository;
import com.defi.auth.group.service.UserInGroupService;
import com.defi.auth.group.repository.GroupRepository;
import com.defi.auth.user.repository.UserRepository;
import com.defi.auth.user.entity.User;
import com.defi.common.CommonMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserInGroupServiceImpl implements UserInGroupService {

    private final UserInGroupRepository userInGroupRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    @Override
    public List<User> findUsersByGroupId(Long groupId, Pageable pageable) {
        return userInGroupRepository.findUsersByGroupId(groupId, pageable);
    }

    @Override
    public void addUserToGroup(Long userId, Long groupId) {
        if (!userRepository.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, CommonMessage.NOT_FOUND);
        }

        if (!groupRepository.existsById(groupId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, CommonMessage.NOT_FOUND);
        }

        UserInGroupId id = new UserInGroupId(userId, groupId);
        if (userInGroupRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, CommonMessage.EXISTING);
        }
        userInGroupRepository.save(new UserInGroup(id));
    }

    @Override
    public void removeUserFromGroup(Long userId, Long groupId) {
        UserInGroupId id = new UserInGroupId(userId, groupId);
        if (!userInGroupRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, CommonMessage.NOT_FOUND);
        }
        userInGroupRepository.deleteById(id);
    }

    @Override
    public List<Long> findGroupIdsByUserId(Long userId) {
        return userInGroupRepository.findGroupIdsByUserId(userId);
    }
}
