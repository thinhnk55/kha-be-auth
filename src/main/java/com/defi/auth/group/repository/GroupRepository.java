package com.defi.auth.group.repository;

import com.defi.auth.group.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
    boolean existsByParentIdAndCode(Long parentId, String code);
}
