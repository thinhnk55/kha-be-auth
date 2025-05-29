package com.defi.auth.permission.controller;

import com.defi.auth.permission.dto.ResourceDto;
import com.defi.auth.permission.dto.ResourceRequest;
import com.defi.auth.permission.service.ResourceService;
import com.defi.common.api.BaseResponse;
import com.defi.common.api.CommonMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/auth/v1/admin/permissions/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    @GetMapping
    public ResponseEntity<BaseResponse<List<ResourceDto>>> getAll() {
        List<ResourceDto> list = resourceService.findAll();
        return ResponseEntity.ok(BaseResponse.of(list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<ResourceDto>> getById(@PathVariable Long id) {
        return resourceService.findById(id)
                .map(resource -> ResponseEntity.ok(BaseResponse.of(resource)))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, CommonMessage.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<BaseResponse<ResourceDto>> create(@RequestBody @Valid ResourceRequest request) {
        ResourceDto resource = resourceService.create(request);
        return ResponseEntity.ok(BaseResponse.of(resource));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<ResourceDto>> update(@PathVariable Long id, @RequestBody @Valid ResourceRequest request) {
        ResourceDto updated = resourceService.update(id, request);
        return ResponseEntity.ok(BaseResponse.of(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<?>> delete(@PathVariable Long id) {
        resourceService.deleteById(id);
        return ResponseEntity.ok(BaseResponse.of("deleted"));
    }
}
