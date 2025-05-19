package com.defi.auth.permission.repository;

import com.defi.auth.permission.entity.ResourceHasAction;
import com.defi.auth.permission.entity.ResourceHasActionId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResourceHasActionRepository extends JpaRepository<ResourceHasAction, ResourceHasActionId> {
    void deleteByIdResourceId(Long resourceId);
    List<ResourceHasAction> findByIdResourceId(Long resourceId);
}
