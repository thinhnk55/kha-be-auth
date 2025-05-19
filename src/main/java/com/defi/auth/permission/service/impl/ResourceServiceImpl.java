package com.defi.auth.permission.service.impl;

import com.defi.auth.permission.dto.ResourceDto;
import com.defi.auth.permission.dto.ResourceRequest;
import com.defi.auth.permission.entity.Resource;
import com.defi.auth.permission.entity.ResourceHasAction;
import com.defi.auth.permission.entity.ResourceHasActionId;
import com.defi.auth.permission.repository.ResourceRepository;
import com.defi.auth.permission.repository.ResourceHasActionRepository;
import com.defi.auth.permission.mapper.ResourceMapper;
import com.defi.auth.permission.service.ResourceService;
import com.defi.common.CommonMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository resourceRepository;
    private final ResourceHasActionRepository resourceHasActionRepository;
    private final ResourceMapper resourceMapper;

    @Override
    public List<ResourceDto> findAll() {
        List<Resource> resources = resourceRepository.findAll();
        return resources.stream()
                .map(this::toResourceDto)
                .toList();
    }

    @Override
    public Optional<ResourceDto> findById(Long id) {
        return resourceRepository.findById(id)
                .map(this::toResourceDto);
    }

    @Override
    @Transactional
    public ResourceDto create(ResourceRequest request) {
        if (resourceRepository.existsByCode(request.getCode())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, CommonMessage.EXISTING);
        }
        Resource resource = resourceMapper.toEntity(request);
        resourceRepository.save(resource);

        List<ResourceHasAction> mappings;
        if (request.getActionIds() != null && !request.getActionIds().isEmpty()) {
            mappings = request.getActionIds().stream()
                    .map(actionId -> ResourceHasAction.builder()
                            .id(ResourceHasActionId.builder()
                                    .resourceId(resource.getId())
                                    .actionId(actionId)
                                    .build())
                            .build())
                    .toList();
            resourceHasActionRepository.saveAll(mappings);
        }
        return toResourceDto(resource);
    }


    @Override
    @Transactional
    public ResourceDto update(Long id, ResourceRequest request) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        CommonMessage.NOT_FOUND));

        if (!resource.getCode().equals(request.getCode())
                && resourceRepository.existsByCode(request.getCode())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, CommonMessage.EXISTING);
        }

        resourceMapper.updateEntity(resource, request);
        resourceRepository.save(resource);

        resourceHasActionRepository.deleteByIdResourceId(resource.getId());

        List<ResourceHasAction> mappings;
        if (request.getActionIds() != null && !request.getActionIds().isEmpty()) {
            mappings = request.getActionIds().stream()
                    .map(actionId -> ResourceHasAction.builder()
                            .id(ResourceHasActionId.builder()
                                    .resourceId(resource.getId())
                                    .actionId(actionId)
                                    .build())
                            .build())
                    .toList();
            resourceHasActionRepository.saveAll(mappings);
        }

        return toResourceDto(resource);
    }


    @Override
    @Transactional
    public void deleteById(Long id) {
        resourceHasActionRepository.deleteByIdResourceId(id);
        resourceRepository.deleteById(id);
    }

    @Override
    public boolean existsByCode(String code) {
        return resourceRepository.existsByCode(code);
    }

    private ResourceDto toResourceDto(Resource resource) {
        List<Long> actionIds = resourceHasActionRepository.findByIdResourceId(resource.getId())
                .stream()
                .map(mapping -> mapping.getId().getActionId())
                .toList();
        return resourceMapper.toDto(resource, actionIds);
    }
}
