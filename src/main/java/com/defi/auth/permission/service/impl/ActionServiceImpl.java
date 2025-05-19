package com.defi.auth.permission.service.impl;

import com.defi.auth.permission.dto.ActionRequest;
import com.defi.auth.permission.entity.Action;
import com.defi.auth.permission.repository.ActionRepository;
import com.defi.auth.permission.mapper.ActionMapper;
import com.defi.auth.permission.service.ActionService;
import com.defi.common.CommonMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActionServiceImpl implements ActionService {
    private final ActionRepository actionRepository;
    private final ActionMapper actionMapper;

    @Override
    public List<Action> findAll() {
        return actionRepository.findAll();
    }

    @Override
    public Optional<Action> findById(Long id) {
        return actionRepository.findById(id);
    }

    @Override
    public Action create(ActionRequest request) {
        if (actionRepository.existsByCode(request.getCode())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, CommonMessage.EXISTING);
        }
        Action action = actionMapper.toEntity(request);
        return actionRepository.save(action);
    }


    @Override
    public Action update(Long id, ActionRequest request) {
        return actionRepository.findById(id).map(existing -> {
            actionMapper.updateEntity(existing, request);
            return actionRepository.save(existing);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, CommonMessage.NOT_FOUND));
    }

    @Override
    public void deleteById(Long id) {
        actionRepository.deleteById(id);
    }

    @Override
    public Optional<Action> findByCode(String code) {
        return Optional.ofNullable(actionRepository.findByCode(code));
    }
}
