package com.defi.auth.group.service.impl;

import com.defi.auth.group.dto.CreateGroupRequest;
import com.defi.auth.group.dto.UpdateGroupMetadataRequest;
import com.defi.auth.group.dto.UpdateGroupRequest;
import com.defi.auth.group.entity.Group;
import com.defi.auth.group.mapper.GroupMapper;
import com.defi.auth.group.repository.GroupRepository;
import com.defi.auth.group.service.GroupService;
import com.defi.common.api.CommonMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;

    private final GroupMapper mapper;

    @Override
    public Group createGroup(CreateGroupRequest req) {
        if (groupRepository.existsByParentIdAndCode(req.getParentId(), req.getCode())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, CommonMessage.EXISTING);
        }
        Group group = mapper.fromCreateRequest(req);
        groupRepository.save(group);
        return group;
    }

    @Override
    public Group updateGroup(Long id, UpdateGroupRequest req) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, CommonMessage.NOT_FOUND));
        group.setName(req.getName());
        groupRepository.save(group);
        return group;
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
    public Group getGroup(Long id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, CommonMessage.NOT_FOUND));
    }

    @Override
    public List<Group> findAll() {
        return groupRepository.findAll();
    }
}


