package com.defi.auth.role.service.impl;

import com.defi.auth.group.repository.GroupRepository;
import com.defi.auth.role.entity.GroupHasRole;
import com.defi.auth.role.entity.GroupHasRoleId;
import com.defi.auth.role.repository.GroupHasRoleRepository;
import com.defi.auth.role.service.GroupHasRoleService;
import com.defi.common.api.CommonMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupHasRoleServiceImpl implements GroupHasRoleService {

    private final GroupHasRoleRepository groupHasRoleRepository;
    private final GroupRepository groupRepository;

    @Override
    @Transactional
    public void assignRoleListToGroup(Long groupId, List<Long> roleList) {
        if (!groupRepository.existsById(groupId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, CommonMessage.NOT_FOUND);
        }
        List<GroupHasRole> toSave = roleList.stream()
                .map(roleId -> GroupHasRole.builder()
                        .id(new GroupHasRoleId(groupId, roleId))
                        .build()
                )
                .toList();
        groupHasRoleRepository.saveAll(toSave);
    }

    @Override
    public void removeRoleFromGroup(Long groupId, Long roleId) {
        GroupHasRoleId id = new GroupHasRoleId(groupId, roleId);
        groupHasRoleRepository.deleteById(id);
    }

    @Override
    public List<GroupHasRole> findAllByIdGroupId(Long groupId) {
        return groupHasRoleRepository.findAllByIdGroupId(groupId);
    }

    @Override
    public List<GroupHasRole> findAllByIdRoleId(Long roleId) {
        return groupHasRoleRepository.findAllByIdRoleId(roleId);
    }

    @Override
    public GroupHasRole getGroupHasRole(Long groupId, Long roleId) {
        GroupHasRoleId id = GroupHasRoleId.builder()
                .groupId(groupId)
                .roleId(roleId)
                .build();
        return groupHasRoleRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, CommonMessage.NOT_FOUND));
    }

    @Override
    public List<Long> findRoleIdsByGroupId(Long groupId) {
        return groupHasRoleRepository.findAllByIdGroupId(groupId)
                .stream()
                .map(groupHasRole -> groupHasRole.getId().getRoleId())
                .toList();
    }

    @Override
    public List<Long> findGroupIdsByRoleId(Long roleId) {
        return groupHasRoleRepository.findAllByIdRoleId(roleId)
                .stream()
                .map(groupHasRole -> groupHasRole.getId().getGroupId())
                .toList();
    }
}