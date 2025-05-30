package com.defi.auth.permission.controller;

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

/**
 * REST controller for policy management operations.
 * 
 * <p>
 * This controller provides endpoints for retrieving policy rules
 * that can be consumed by external systems or other microservices
 * for policy synchronization.
 * </p>
 * 
 * @author Defi Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/auth/v1/public/policies")
@RequiredArgsConstructor
@Slf4j
public class PolicyController {

    private final DatabasePolicyLoader databasePolicyLoader;

    /**
     * Retrieves policy rules filtered by resource codes.
     * 
     * <p>
     * This endpoint is designed for microservices to fetch only
     * the policies relevant to their domain resources, improving
     * performance and reducing data transfer.
     * </p>
     * 
     * <p>
     * Examples:
     * </p>
     * <ul>
     * <li>GET /api/v1/policies - returns all policies</li>
     * <li>GET /api/v1/policies?resources=users - returns policies for 'users'
     * resource</li>
     * <li>GET /api/v1/policies?resources=users,roles - returns policies for 'users'
     * and 'roles'</li>
     * </ul>
     * 
     * @param resources comma-separated list of resource codes to filter by
     *                  (optional)
     * @return BaseResponse containing list of policy rules
     */
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

    /**
     * Parses the resources parameter into a list of resource codes.
     * 
     * @param resources comma-separated resource codes (can be null or empty)
     * @return list of resource codes, empty list if no resources specified
     */
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