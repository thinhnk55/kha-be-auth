package com.defi.auth.role.service.impl;

import com.defi.auth.role.entity.GroupHasRole;
import com.defi.auth.role.entity.GroupHasRoleId;
import com.defi.auth.role.repository.GroupHasRoleRepository;
import com.defi.auth.role.service.GroupHasRoleService;
import com.defi.common.api.CommonMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupHasRoleServiceImpl implements GroupHasRoleService {

    private final GroupHasRoleRepository groupHasRoleRepository;

    @Override
    public void assignRoleToGroup(Long groupId, Long roleId) {
        GroupHasRoleId id = new GroupHasRoleId(groupId, roleId);
        if (groupHasRoleRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, CommonMessage.EXISTING);
        }
        GroupHasRole entity = GroupHasRole.builder()
                .id(id)
                .build();
        groupHasRoleRepository.save(entity);
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