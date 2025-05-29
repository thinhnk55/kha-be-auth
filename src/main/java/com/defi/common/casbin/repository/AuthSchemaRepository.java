package com.defi.common.casbin.repository;

import com.defi.common.casbin.model.PolicyRule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Repository for querying auth schema from other services.
 * Handles cross-schema queries to load permissions data using policy_rules
 * view.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class AuthSchemaRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * RowMapper for PolicyRule to avoid deprecated query methods
     */
    private static final RowMapper<PolicyRule> POLICY_RULE_ROW_MAPPER = new RowMapper<PolicyRule>() {
        @Override
        public PolicyRule mapRow(ResultSet rs, int rowNum) throws SQLException {
            return PolicyRule.builder()
                    .id(rs.getLong("id"))
                    .roleId(rs.getLong("role_id"))
                    .resourceCode(rs.getString("resource_code"))
                    .actionCode(rs.getString("action_code"))
                    .build();
        }
    };

    /**
     * Find permissions by resource codes with cross-schema query using policy_rules
     * view.
     * This is the main method used by services to load their relevant policies.
     */
    public List<PolicyRule> findPermissionsByResourceCodes(List<String> resourceCodes) {
        if (resourceCodes == null || resourceCodes.isEmpty()) {
            log.warn("No resource codes provided, returning empty list");
            return List.of();
        }

        log.debug("Loading permissions for resources: {}", resourceCodes);

        try {
            // Create placeholders for IN clause
            String placeholders = String.join(",", resourceCodes.stream()
                    .map(r -> "?").toList());

            String sql = """
                    SELECT id, role_id, resource_code, action_code
                    FROM auth.policy_rules
                    WHERE resource_code IN (%s)
                    ORDER BY id
                    """.formatted(placeholders);

            List<PolicyRule> policies = jdbcTemplate.query(sql, POLICY_RULE_ROW_MAPPER, resourceCodes.toArray());

            log.info("Loaded {} policies for resources: {}", policies.size(), resourceCodes);
            return policies;

        } catch (Exception e) {
            log.error("Failed to load permissions for resources: {}", resourceCodes, e);
            throw new RuntimeException("Cross-schema permission query failed", e);
        }
    }

    /**
     * Find all permissions from auth schema (fallback method).
     * Used when Redis is down and full reload is needed.
     */
    public List<PolicyRule> findAllPermissions() {
        log.info("Loading all permissions from auth schema (fallback mode)");

        try {
            String sql = """
                    SELECT id, role_id, resource_code, action_code
                    FROM auth.policy_rules
                    ORDER BY id
                    """;

            List<PolicyRule> policies = jdbcTemplate.query(sql, POLICY_RULE_ROW_MAPPER);

            log.info("Loaded {} total policies from auth schema", policies.size());
            return policies;

        } catch (Exception e) {
            log.error("Failed to load all permissions from auth schema", e);
            throw new RuntimeException("Full permission query failed", e);
        }
    }

    /**
     * Find permissions by resource codes for current schema (auth service only).
     * This method is used when running within auth service (no cross-schema
     * needed).
     */
    public List<PolicyRule> findPermissionsByResourceCodesLocal(List<String> resourceCodes) {
        if (resourceCodes == null || resourceCodes.isEmpty()) {
            log.warn("No resource codes provided, returning empty list");
            return List.of();
        }

        log.debug("Loading permissions for resources (local): {}", resourceCodes);

        try {
            // Create placeholders for IN clause
            String placeholders = String.join(",", resourceCodes.stream()
                    .map(r -> "?").toList());

            String sql = """
                    SELECT id, role_id, resource_code, action_code
                    FROM policy_rules
                    WHERE resource_code IN (%s)
                    ORDER BY id
                    """.formatted(placeholders);

            List<PolicyRule> policies = jdbcTemplate.query(sql, POLICY_RULE_ROW_MAPPER, resourceCodes.toArray());

            log.info("Loaded {} policies for resources (local): {}", policies.size(), resourceCodes);
            return policies;

        } catch (Exception e) {
            log.error("Failed to load permissions for resources (local): {}", resourceCodes, e);
            throw new RuntimeException("Local permission query failed", e);
        }
    }

    /**
     * Find all permissions for current schema (auth service only).
     */
    public List<PolicyRule> findAllPermissionsLocal() {
        log.info("Loading all permissions from local schema");

        try {
            String sql = """
                    SELECT id, role_id, resource_code, action_code
                    FROM policy_rules
                    ORDER BY id
                    """;

            List<PolicyRule> policies = jdbcTemplate.query(sql, POLICY_RULE_ROW_MAPPER);

            log.info("Loaded {} total policies from local schema", policies.size());
            return policies;

        } catch (Exception e) {
            log.error("Failed to load all permissions from local schema", e);
            throw new RuntimeException("Local full permission query failed", e);
        }
    }

    /**
     * Check database connectivity to auth schema
     */
    public boolean isAuthSchemaAccessible() {
        try {
            Integer result = jdbcTemplate.queryForObject("SELECT 1 FROM auth.policy_rules LIMIT 1", Integer.class);
            return result != null && result == 1;
        } catch (Exception e) {
            log.warn("Auth schema is not accessible: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Check local schema connectivity (for auth service)
     */
    public boolean isLocalSchemaAccessible() {
        try {
            Integer result = jdbcTemplate.queryForObject("SELECT 1 FROM policy_rules LIMIT 1", Integer.class);
            return result != null && result == 1;
        } catch (Exception e) {
            log.warn("Local schema is not accessible: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Get total permission count for monitoring (auth schema)
     */
    public long getTotalPermissionCount() {
        try {
            Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM auth.policy_rules", Long.class);
            return count != null ? count : 0;
        } catch (Exception e) {
            log.error("Failed to get permission count", e);
            return -1;
        }
    }

    /**
     * Get total permission count for monitoring (local schema)
     */
    public long getTotalPermissionCountLocal() {
        try {
            Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM policy_rules", Long.class);
            return count != null ? count : 0;
        } catch (Exception e) {
            log.error("Failed to get local permission count", e);
            return -1;
        }
    }

    /**
     * Get permissions count by resource codes for monitoring
     */
    public long getPermissionCountByResources(List<String> resourceCodes) {
        if (resourceCodes == null || resourceCodes.isEmpty()) {
            return 0;
        }

        try {
            String placeholders = String.join(",", resourceCodes.stream()
                    .map(r -> "?").toList());

            String sql = """
                    SELECT COUNT(*) FROM auth.policy_rules
                    WHERE resource_code IN (%s)
                    """.formatted(placeholders);

            Long count = jdbcTemplate.queryForObject(sql, Long.class, resourceCodes.toArray());
            return count != null ? count : 0;
        } catch (Exception e) {
            log.error("Failed to get permission count for resources: {}", resourceCodes, e);
            return -1;
        }
    }
}