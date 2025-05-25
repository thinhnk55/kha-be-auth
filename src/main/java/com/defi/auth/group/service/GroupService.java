package com.defi.auth.group.service;

import com.defi.auth.group.dto.CreateGroupRequest;
import com.defi.auth.group.dto.GroupDto;
import com.defi.auth.group.dto.UpdateGroupMetadataRequest;
import com.defi.auth.group.dto.UpdateGroupRequest;
import com.defi.auth.user.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GroupService {
    GroupDto createGroup(CreateGroupRequest req);
    GroupDto updateGroup(Long id, UpdateGroupRequest req);
    void updateMetadata(Long id, UpdateGroupMetadataRequest req);
    void deleteGroup(Long id);
    GroupDto getGroup(Long id);
    List<GroupDto> findAll();
    List<User> getUsersByGroup(Long groupId, Pageable pageable);

    void addUserToGroup(Long userId, Long groupId);

    void removeUserFromGroup(Long userId, Long groupId);
}
