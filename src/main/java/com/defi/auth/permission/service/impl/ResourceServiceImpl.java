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
import com.defi.common.api.CommonMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository resourceRepository;
    private final ResourceHasActionRepository resourceHasActionRepository;
    private final ResourceMapper resourceMapper;
    private final ObjectMapper objectMapper;

    @Override
    public List<ResourceDto> findAll() {
        List<Object[]> results = resourceRepository.findAllResourcesWithActions();

        return results.stream()
                .map(this::convertToDto)
                .toList();
    }

    private ResourceDto convertToDto(Object[] row) {
        try {
            log.debug("Converting row: {}", Arrays.toString(row));

            ResourceDto dto = new ResourceDto();
            dto.setId(((Number) row[0]).longValue());
            dto.setCreated_at(((Number) row[1]).longValue());
            dto.setUpdated_at(((Number) row[2]).longValue());
            dto.setCode((String) row[3]);
            dto.setName((String) row[4]);

            // Parse metadata safely
            try {
                if (row[5] != null) {
                    if (row[5] instanceof String) {
                        dto.setMetadata(objectMapper.readValue((String) row[5], ObjectNode.class));
                    } else if (row[5] instanceof ObjectNode) {
                        dto.setMetadata((ObjectNode) row[5]);
                    } else {
                        log.warn("Unexpected metadata type: {}", row[5].getClass());
                        dto.setMetadata(null);
                    }
                } else {
                    dto.setMetadata(null);
                }
            } catch (Exception e) {
                log.error("Error parsing metadata: {}", e.getMessage());
                dto.setMetadata(null);
            }

            // Parse action IDs from comma-separated string
            String actionIdsStr = (String) row[6];
            if (actionIdsStr != null && !actionIdsStr.trim().isEmpty()) {
                List<Long> actions = Arrays.stream(actionIdsStr.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .map(Long::parseLong)
                        .toList();
                dto.setActions(actions);
            } else {
                dto.setActions(List.of());
            }

            return dto;
        } catch (Exception e) {
            log.error("Error converting row to ResourceDto: ", e);
            throw new RuntimeException("Error converting data", e);
        }
    }

    @Override
    public Optional<ResourceDto> findById(Long id) {
        return resourceRepository.findById(id)
                .map(resource -> {
                    List<Long> actionIds = resourceHasActionRepository.findByIdResourceId(resource.getId())
                            .stream()
                            .map(mapping -> mapping.getId().getActionId())
                            .toList();
                    return resourceMapper.toDto(resource, actionIds);
                });
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
        if (request.getActions() != null && !request.getActions().isEmpty()) {
            mappings = request.getActions().stream()
                    .map(actionId -> ResourceHasAction.builder()
                            .id(ResourceHasActionId.builder()
                                    .resourceId(resource.getId())
                                    .actionId(actionId)
                                    .build())
                            .build())
                    .toList();
            resourceHasActionRepository.saveAll(mappings);
        }

        // Return DTO với actions đã được save
        List<Long> actionIds = request.getActions() != null ? request.getActions() : List.of();
        return resourceMapper.toDto(resource, actionIds);
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
        if (request.getActions() != null && !request.getActions().isEmpty()) {
            mappings = request.getActions().stream()
                    .map(actionId -> ResourceHasAction.builder()
                            .id(ResourceHasActionId.builder()
                                    .resourceId(resource.getId())
                                    .actionId(actionId)
                                    .build())
                            .build())
                    .toList();
            resourceHasActionRepository.saveAll(mappings);
        }

        // Return DTO với actions đã được update
        List<Long> actionIds = request.getActions() != null ? request.getActions() : List.of();
        return resourceMapper.toDto(resource, actionIds);
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
}
