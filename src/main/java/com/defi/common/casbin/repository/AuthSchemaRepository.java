package com.defi.common.casbin.repository;

import com.defi.common.casbin.entity.PolicyRule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Repository for querying policy rules from the database.
 * 
 * <p>This repository provides access to the {@code policy_rules} view, which combines
 * data from permissions, resources, and actions tables to provide a simplified interface
 * for loading Casbin policies.</p>
 * 
 * <p>Key features:</p>
 * <ul>
 *   <li>Unified access to policy data through database view</li>
 *   <li>Resource-based filtering for microservice architectures</li>
 *   <li>Optimized batch loading of policies</li>
 *   <li>Connection health checking</li>
 * </ul>
 * 
 * @author Defi Team
 * @since 1.0.0
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class AuthSchemaRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * RowMapper for converting database rows to PolicyRule objects.
     * 
     * <p>Maps the following database columns:</p>
     * <ul>
     *   <li>{@code id} → {@link PolicyRule#getId()}</li>
     *   <li>{@code role_id} → {@link PolicyRule#getRoleId()}</li>
     *   <li>{@code resource_code} → {@link PolicyRule#getResourceCode()}</li>
     *   <li>{@code action_code} → {@link PolicyRule#getActionCode()}</li>
     * </ul>
     */
    private static final RowMapper<PolicyRule> POLICY_RULE_ROW_MAPPER = new RowMapper<PolicyRule>() {
        @Override
        public PolicyRule mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            return PolicyRule.builder()
                    .id(rs.getLong("id"))
                    .roleId(rs.getLong("role_id"))
                    .resourceCode(rs.getString("resource_code"))
                    .actionCode(rs.getString("action_code"))
                    .build();
        }
    };

    /**
     * Finds permissions by resource codes with optimized batch query.
     * 
     * <p>This method is used for resource-based filtering in microservice architectures
     * where each service only needs policies for specific resources it manages.</p>
     * 
     * @param resourceCodes list of resource codes to filter by
     * @return list of policy rules matching the specified resources, ordered by ID
     * @throws RuntimeException if the database query fails
     */
    public List<PolicyRule> findPermissionsByResourceCodes(List<String> resourceCodes) {
        if (resourceCodes == null || resourceCodes.isEmpty()) {
            log.warn("No resource codes provided, returning empty list");
            return List.of();
        }

        log.debug("Loading permissions for resources: {}", resourceCodes);

        try {
            String placeholders = String.join(",", resourceCodes.stream()
                    .map(r -> "?").toList());

            String sql = """
                    SELECT id, role_id, resource_code, action_code
                    FROM policy_rules
                    WHERE resource_code IN (%s)
                    ORDER BY id
                    """.formatted(placeholders);

            List<PolicyRule> policies = jdbcTemplate.query(sql, POLICY_RULE_ROW_MAPPER, resourceCodes.toArray());

            log.info("Loaded {} policies for resources: {}", policies.size(), resourceCodes);
            return policies;

        } catch (Exception e) {
            log.error("Failed to load permissions for resources: {}", resourceCodes, e);
            throw new RuntimeException("Permission query failed", e);
        }
    }

    /**
     * Finds all permissions in the system.
     * 
     * <p>This method loads all available policy rules without any filtering.
     * Use with caution in large systems as it may return a significant amount of data.</p>
     * 
     * @return list of all policy rules, ordered by ID
     * @throws RuntimeException if the database query fails
     */
    public List<PolicyRule> findAllPermissions() {
        log.info("Loading all permissions");

        try {
            String sql = """
                    SELECT id, role_id, resource_code, action_code
                    FROM policy_rules
                    ORDER BY id
                    """;

            List<PolicyRule> policies = jdbcTemplate.query(sql, POLICY_RULE_ROW_MAPPER);

            log.info("Loaded {} total policies", policies.size());
            return policies;

        } catch (Exception e) {
            log.error("Failed to load all permissions", e);
            throw new RuntimeException("Full permission query failed", e);
        }
    }

    /**
     * Checks database connectivity by executing a simple test query.
     * 
     * <p>This method verifies that the {@code policy_rules} view is accessible
     * and the database connection is working properly.</p>
     * 
     * @return true if database is accessible and policy_rules view exists, false otherwise
     */
    public boolean isDatabaseAccessible() {
        try {
            Integer result = jdbcTemplate.queryForObject("SELECT 1 FROM policy_rules LIMIT 1", Integer.class);
            return result != null && result == 1;
        } catch (Exception e) {
            log.warn("Database is not accessible: {}", e.getMessage());
            return false;
        }
    }
}