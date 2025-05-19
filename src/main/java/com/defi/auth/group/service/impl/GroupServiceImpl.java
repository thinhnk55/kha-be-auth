package com.defi.auth.group.service.impl;

import com.defi.auth.group.dto.CreateGroupRequest;
import com.defi.auth.group.dto.GroupDTO;
import com.defi.auth.group.dto.UpdateGroupMetadataRequest;
import com.defi.auth.group.dto.UpdateGroupRequest;
import com.defi.auth.group.entity.Group;
import com.defi.auth.group.entity.UserInGroup;
import com.defi.auth.group.entity.UserInGroupId;
import com.defi.auth.group.mapper.GroupMapper;
import com.defi.auth.group.repository.GroupRepository;
import com.defi.auth.group.repository.UserInGroupRepository;
import com.defi.auth.group.service.GroupService;
import com.defi.auth.user.entity.User;
import com.defi.auth.user.repository.UserRepository;
import com.defi.common.CommonMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final UserInGroupRepository userInGroupRepository;
    private final GroupMapper groupMapper;

    @Override
    public GroupDTO createGroup(CreateGroupRequest req) {
        if (groupRepository.existsByParentIdAndCode(req.getParentId(), req.getCode())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, CommonMessage.EXISTING);
        }
        Group group = groupMapper.fromCreateRequest(req);
        groupRepository.save(group);

        return groupMapper.toDTO(group);
    }


    @Override
    public GroupDTO updateGroup(Long id, UpdateGroupRequest req) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, CommonMessage.NOT_FOUND));
        group.setName(req.getName());
        groupRepository.save(group);
        return groupMapper.toDTO(group);
    }

    @Override
    public void updateMetadata(Long id, UpdateGroupMetadataRequest req) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, CommonMessage.NOT_FOUND));
        group.setMetadata(req.getMetadata());
        groupRepository.save(group);
    }

    @Override
    public void deleteGroup(Long id) {
        groupRepository.deleteById(id);
    }

    @Override
    public GroupDTO getGroup(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, CommonMessage.NOT_FOUND));
        return groupMapper.toDTO(group);
    }

    @Override
    public List<GroupDTO> findAll() {
        List<Group> groups = groupRepository.findAll();
        return groups.stream().map(groupMapper::toDTO).toList();
    }

    @Override
    public List<User> getUsersByGroup(Long groupId, Pageable pageable) {
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
}

