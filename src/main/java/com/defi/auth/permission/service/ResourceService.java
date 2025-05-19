package com.defi.auth.permission.service;

import com.defi.auth.permission.dto.ResourceDto;
import com.defi.auth.permission.dto.ResourceRequest;

import java.util.List;
import java.util.Optional;

public interface ResourceService {
    List<ResourceDto> findAll();

    Optional<ResourceDto> findById(Long id);

    ResourceDto create(ResourceRequest request);

    ResourceDto update(Long id, ResourceRequest request);

    void deleteById(Long id);

    boolean existsByCode(String code);
}
