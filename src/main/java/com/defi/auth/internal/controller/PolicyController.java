package com.defi.auth.internal.controller;

import com.defi.common.api.BaseResponse;
import com.defi.common.casbin.entity.PolicyRule;
import com.defi.common.casbin.service.DatabasePolicyLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/auth/v1/internal/policies")
@RequiredArgsConstructor
@Slf4j
public class PolicyController {

    private final DatabasePolicyLoader databasePolicyLoader;
    @GetMapping
    public ResponseEntity<BaseResponse<List<PolicyRule>>> getPolicies(
            @RequestParam(required = false) String resources) {

        log.info("Fetching policies with resources filter: {}", resources);
        String sql = "SELECT * FROM policy_rules";

        // Parse resources parameter (comma-separated)
        List<String> resourceFilter = parseResourcesParameter(resources);

        List<PolicyRule> policies = databasePolicyLoader.loadPolicyRulesFromDatabase(sql, resourceFilter);

        log.info("Successfully fetched {} policies for resources: {}", policies.size(), resourceFilter);

        return ResponseEntity.ok(BaseResponse.of(policies));
    }
    private List<String> parseResourcesParameter(String resources) {
        if (resources == null || resources.trim().isEmpty()) {
            return Collections.emptyList();
        }

        // Split by comma and trim whitespace
        return Arrays.stream(resources.split(","))
                .map(String::trim)
                .filter(resource -> !resource.isEmpty())
                .toList();
    }
}