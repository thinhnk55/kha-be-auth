package com.defi.auth.internal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service for managing version numbers in the auth system.
 * 
 * <p>
 * This service handles version tracking for different auth components
 * to enable efficient polling and caching strategies across microservices.
 * </p>
 * 
 * @author Defi Team
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class VersionService {

    private final JdbcTemplate jdbcTemplate;

    /** Version code for policy changes */
    public static final String POLICY_VERSION_CODE = "policy_version";

    /**
     * Gets the current version for a specific component code.
     * 
     * @param code the component code (e.g., "policy_version")
     * @return current version number, or empty if code not found
     */
    public Optional<Long> getCurrentVersion(String code) {
        try {
            String sql = "SELECT version FROM auth_version WHERE code = ?";
            Long version = jdbcTemplate.queryForObject(sql, Long.class, code);
            return Optional.ofNullable(version);
        } catch (Exception e) {
            log.warn("Failed to get version for code: {}", code, e);
            return Optional.empty();
        }
    }

    /**
     * Increments the version for a specific component code.
     * 
     * <p>
     * This method is transactional to ensure consistency when
     * updating both policies and version number.
     * </p>
     * 
     * @param code the component code to increment
     * @return new version number after increment
     * @throws RuntimeException if update fails
     */
    @Transactional
    public long incrementVersion(String code) {
        try {
            String sql = "UPDATE auth_version SET version = version + 1 WHERE code = ?";
            int updated = jdbcTemplate.update(sql, code);

            if (updated == 0) {
                // Code doesn't exist, insert it with version 1
                String insertSql = "INSERT INTO auth_version (code, version) VALUES (?, 1)";
                jdbcTemplate.update(insertSql, code);
                log.info("Created new version entry for code: {} with version 1", code);
                return 1L;
            }

            // Get the new version
            Long newVersion = getCurrentVersion(code)
                    .orElseThrow(() -> new RuntimeException("Failed to get version after increment"));

            log.info("Incremented version for code: {} to version: {}", code, newVersion);
            return newVersion;

        } catch (Exception e) {
            log.error("Failed to increment version for code: {}", code, e);
            throw new RuntimeException("Version increment failed", e);
        }
    }

    /**
     * Increments the policy version specifically.
     * 
     * <p>
     * This is a convenience method for the most common use case.
     * </p>
     * 
     * @return new policy version number
     */
    @Transactional
    public long incrementPolicyVersion() {
        return incrementVersion(POLICY_VERSION_CODE);
    }

    /**
     * Gets the current policy version.
     * 
     * @return current policy version, or 0 if not found
     */
    public long getCurrentPolicyVersion() {
        return getCurrentVersion(POLICY_VERSION_CODE).orElse(0L);
    }

    /**
     * Initializes version for a component if it doesn't exist.
     * 
     * @param code           the component code
     * @param initialVersion the initial version number
     */
    @Transactional
    public void initializeVersionIfNotExists(String code, long initialVersion) {
        try {
            String checkSql = "SELECT COUNT(*) FROM auth_version WHERE code = ?";
            Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, code);

            if (count == null || count == 0) {
                String insertSql = "INSERT INTO auth_version (code, version) VALUES (?, ?)";
                jdbcTemplate.update(insertSql, code, initialVersion);
                log.info("Initialized version for code: {} with version: {}", code, initialVersion);
            }
        } catch (Exception e) {
            log.error("Failed to initialize version for code: {}", code, e);
        }
    }
}