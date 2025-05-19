package com.defi.auth.permission.service;

import com.defi.auth.permission.dto.ActionRequest;
import com.defi.auth.permission.entity.Action;

import java.util.List;
import java.util.Optional;

public interface ActionService {
    List<Action> findAll();

    Optional<Action> findById(Long id);

    Action create(ActionRequest request);

    Action update(Long id, ActionRequest request);

    void deleteById(Long id);

    Optional<Action> findByCode(String code);
}

