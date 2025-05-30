package com.defi.auth.internal.controller;

import com.defi.auth.internal.service.VersionService;
import com.defi.common.api.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth/v1/internal/version")
@RequiredArgsConstructor
@Slf4j
public class VersionController {

    private final VersionService versionService;

    @GetMapping("/{code}")
    public ResponseEntity<BaseResponse<Long>> getVersion(@PathVariable String code) {
        Optional<Long> version = versionService.getCurrentVersion(code);
        long versionValue = version.orElse(0L);
        return ResponseEntity.ok(BaseResponse.of(versionValue));
    }
}