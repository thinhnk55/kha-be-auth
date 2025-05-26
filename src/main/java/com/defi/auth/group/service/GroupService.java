package com.defi.auth.group.service;

import com.defi.auth.group.dto.CreateGroupRequest;
import com.defi.auth.group.dto.UpdateGroupMetadataRequest;
import com.defi.auth.group.dto.UpdateGroupRequest;
import com.defi.auth.group.entity.Group;

import java.util.List;

public interface GroupService {
    Group createGroup(CreateGroupRequest req);
    Group updateGroup(Long id, UpdateGroupRequest req);
    void updateMetadata(Long id, UpdateGroupMetadataRequest req);
    void deleteGroup(Long id);
    Group getGroup(Long id);
    List<Group> findAll();
}
