package com.defi.common.casbin.service;

import com.defi.common.casbin.config.CasbinProperties;
import com.defi.common.casbin.model.PolicyRule;
import com.defi.common.casbin.repository.AuthSchemaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.casbin.jcasbin.main.Enforcer;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service responsible for loading policies into Casbin enforcer from database.
 * Simplified architecture without Redis cache - policies are cached in enforcer
 * memory.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PolicyLoader {

    private final AuthSchemaRepository authSchemaRepository;
    private final CasbinProperties casbinProperties;

    /**
     * Load policies into enforcer from database.
     * Uses local schema for auth service, cross-schema for other services.
     */
    public void loadPolicies(Enforcer enforcer) {
        String serviceName = casbinProperties.getServiceName();
        List<String> resources = casbinProperties.getResources();

        log.info("Loading policies for service: {} with resources: {}", serviceName, resources);

        try {
            List<PolicyRule> policies = loadPoliciesFromDatabase(resources, serviceName);

            // Clear existing policies first
            enforcer.clearPolicy();

            // Load new policies into enforcer
            loadPoliciesIntoEnforcer(enforcer, policies);

            log.info("Policy loading completed for service: {} - {} policies loaded",
                    serviceName, policies.size());

        } catch (Exception e) {
            log.error("Failed to load policies for service: {}", serviceName, e);
            throw new RuntimeException("Policy loading failed", e);
        }
    }

    /**
     * Load policies from database based on service type and resources
     */
    private List<PolicyRule> loadPoliciesFromDatabase(List<String> resources, String serviceName) {
        boolean isAuthService = "auth-service".equals(serviceName);

        if (!casbinProperties.isEnableFiltering() || resources.isEmpty()) {
            // Load all policies
            if (isAuthService) {
                log.debug("Loading all policies from local schema (auth service)");
                return authSchemaRepository.findAllPermissionsLocal();
            } else {
                log.debug("Loading all policies from auth schema (other service)");
                return authSchemaRepository.findAllPermissions();
            }
        } else {
            // Load filtered policies by resources
            if (isAuthService) {
                log.debug("Loading filtered policies from local schema for resources: {}", resources);
                return authSchemaRepository.findPermissionsByResourceCodesLocal(resources);
            } else {
                log.debug("Loading filtered policies from auth schema for resources: {}", resources);
                return authSchemaRepository.findPermissionsByResourceCodes(resources);
            }
        }
    }

    /**
     * Load policies into Casbin enforcer
     */
    private void loadPoliciesIntoEnforcer(Enforcer enforcer, List<PolicyRule> policies) {
        log.debug("Loading {} policies into enforcer", policies.size());

        int successCount = 0;
        int failureCount = 0;

        for (PolicyRule policy : policies) {
            try {
                String[] casbinPolicy = policy.toCasbinPolicy();
                boolean added = enforcer.addPolicy(casbinPolicy);

                if (added) {
                    successCount++;
                    log.debug("Added policy: {}", policy);
                } else {
                    log.debug("Policy already exists, skipped: {}", policy);
                }

            } catch (Exception e) {
                failureCount++;
                log.error("Failed to add policy to enforcer: {}", policy, e);
            }
        }

        log.info("Policy loading into enforcer completed: {} success, {} failures",
                successCount, failureCount);

        if (failureCount > 0) {
            log.warn("Some policies failed to load, enforcer may not have complete policy set");
        }
    }

    /**
     * Check if database is available
     */
    public boolean isDatabaseAvailable() {
        String serviceName = casbinProperties.getServiceName();
        boolean isAuthService = "auth-service".equals(serviceName);

        if (isAuthService) {
            return authSchemaRepository.isLocalSchemaAccessible();
        } else {
            return authSchemaRepository.isAuthSchemaAccessible();
        }
    }

    /**
     * Get current policy count from enforcer
     */
    public int getCurrentPolicyCount(Enforcer enforcer) {
        try {
            return enforcer.getPolicy().size();
        } catch (Exception e) {
            log.error("Failed to get policy count from enforcer", e);
            return -1;
        }
    }

    /**
     * Get database policy count for monitoring
     */
    public long getDatabasePolicyCount() {
        String serviceName = casbinProperties.getServiceName();
        boolean isAuthService = "auth-service".equals(serviceName);

        if (isAuthService) {
            return authSchemaRepository.getTotalPermissionCountLocal();
        } else {
            return authSchemaRepository.getTotalPermissionCount();
        }
    }

    /**
     * Get database policy count for specific resources
     */
    public long getDatabasePolicyCountForResources(List<String> resources) {
        if (resources == null || resources.isEmpty()) {
            return getDatabasePolicyCount();
        }

        return authSchemaRepository.getPermissionCountByResources(resources);
    }

    /**
     * Health check - simplified to only check database
     */
    public boolean isHealthy() {
        return isDatabaseAvailable();
    }
}