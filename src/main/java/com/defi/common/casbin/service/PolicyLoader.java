package com.defi.common.casbin.service;

import com.defi.common.casbin.config.CasbinProperties;
import com.defi.common.casbin.entity.PolicyRule;
import com.defi.common.casbin.repository.AuthSchemaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.casbin.jcasbin.main.Enforcer;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service responsible for loading policies into Casbin enforcer from database.
 * 
 * <p>This service handles the complete policy loading process, including:</p>
 * <ul>
 *   <li>Loading policies from database based on configuration</li>
 *   <li>Resource-based filtering for microservice architectures</li>
 *   <li>Batch loading into Casbin enforcer for optimal performance</li>
 *   <li>Policy synchronization and cache management</li>
 * </ul>
 * 
 * <p>The service supports two loading modes:</p>
 * <ul>
 *   <li><strong>Filtered loading</strong>: Loads only policies for specific resources (recommended)</li>
 *   <li><strong>Full loading</strong>: Loads all policies (use for central services)</li>
 * </ul>
 * 
 * @author Defi Team
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PolicyLoader {

    private final AuthSchemaRepository authSchemaRepository;
    private final CasbinProperties casbinProperties;

    /**
     * Loads policies into the Casbin enforcer from database.
     * 
     * <p>This method performs a complete policy reload:</p>
     * <ol>
     *   <li>Determines which policies to load based on configuration</li>
     *   <li>Clears existing policies from enforcer</li>
     *   <li>Loads new policies from database</li>
     *   <li>Batch inserts policies into enforcer for optimal performance</li>
     * </ol>
     * 
     * <p>The loading strategy depends on {@link CasbinProperties#isEnableFiltering()} and
     * {@link CasbinProperties#getResources()}:</p>
     * <ul>
     *   <li>If filtering is disabled or no resources specified: loads all policies</li>
     *   <li>If filtering is enabled with resources: loads only policies for those resources</li>
     * </ul>
     * 
     * @param enforcer the Casbin enforcer to load policies into
     * @throws RuntimeException if policy loading fails
     */
    public void loadPolicies(Enforcer enforcer) {
        List<String> resources = casbinProperties.getResources();

        log.info("Loading policies for resources: {}", resources);

        try {
            List<PolicyRule> policies = loadPoliciesFromDatabase(resources);

            // Clear existing policies first
            enforcer.clearPolicy();

            // Load new policies into enforcer
            loadPoliciesIntoEnforcer(enforcer, policies);

            log.info("Policy loading completed for service: {} - {} policies loaded", policies.size());

        } catch (Exception e) {
            log.error("Failed to load policies for service: {}", e);
            throw new RuntimeException("Policy loading failed", e);
        }
    }

    /**
     * Loads policies from database based on resource filtering configuration.
     * 
     * @param resources list of resource codes to filter by, or empty for all policies
     * @return list of policy rules loaded from database
     */
    private List<PolicyRule> loadPoliciesFromDatabase(List<String> resources) {
        return authSchemaRepository.findPermissionsByResourceCodes(resources);
    }

    /**
     * Loads policies into Casbin enforcer using batch operations for optimal performance.
     * 
     * <p>This method converts all policy rules to Casbin format and performs a single
     * batch insert operation, which is significantly faster than individual inserts
     * when dealing with large numbers of policies.</p>
     * 
     * @param enforcer the Casbin enforcer to load policies into
     * @param policies list of policy rules to load
     * @throws RuntimeException if batch loading fails
     */
    private void loadPoliciesIntoEnforcer(Enforcer enforcer, List<PolicyRule> policies) {
        log.debug("Loading {} policies into enforcer", policies.size());

        if (policies.isEmpty()) {
            log.info("No policies to load");
            return;
        }

        try {
            // Convert all policies to Casbin format
            String[][] casbinPolicies = policies.stream()
                    .map(PolicyRule::toCasbinPolicy)
                    .toArray(String[][]::new);

            // Add all policies in batch
            boolean success = enforcer.addPolicies(casbinPolicies);

            if (success) {
                log.info("Successfully loaded {} policies into enforcer", policies.size());
            } else {
                log.warn("Some policies may have failed to load or already existed");
            }

        } catch (Exception e) {
            log.error("Failed to load policies into enforcer", e);
            throw new RuntimeException("Policy loading into enforcer failed", e);
        }
    }
}