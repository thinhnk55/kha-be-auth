package com.defi.common.casbin.service;

import com.defi.common.casbin.config.CasbinProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.casbin.jcasbin.main.Enforcer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Service responsible for handling policy reloads with retry mechanism.
 * Simplified for string-based event handling instead of complex events.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PolicyUpdateService {

    private final PolicyLoader policyLoader;
    private final CasbinProperties casbinProperties;
    private final Enforcer enforcer;

    // Retry state management
    private final AtomicInteger retryCount = new AtomicInteger(0);
    private final AtomicReference<LocalDateTime> lastFailureTime = new AtomicReference<>();
    private final AtomicReference<String> lastFailureReason = new AtomicReference<>();

    private static final int MAX_RETRY_COUNT = 3;
    private static final int RETRY_INTERVAL_MINUTES = 5;

    /**
     * Handle policy reload from Redis event.
     * Simplified - no complex event processing, just reload.
     */
    @Async
    public void reloadPoliciesFromEvent(String context) {
        if (!casbinProperties.isAutoReload()) {
            log.debug("Auto-reload is disabled, ignoring policy reload event");
            return;
        }

        log.info("Processing policy reload event with context: {}", context);

        try {
            reloadPolicies("Event: " + context);
            resetRetryState();

        } catch (Exception e) {
            log.error("Failed to reload policies for event context: {}", context, e);
            handleReloadFailure(e, "Event-triggered reload failed: " + context);
        }
    }

    /**
     * Manual policy reload (for administrative purposes)
     */
    public void reloadPoliciesManually() {
        log.info("Manual policy reload requested");

        try {
            reloadPolicies("Manual reload");
            resetRetryState();

        } catch (Exception e) {
            log.error("Manual policy reload failed", e);
            handleReloadFailure(e, "Manual reload failed");
            throw new RuntimeException("Manual policy reload failed", e);
        }
    }

    /**
     * Core method to reload policies from database
     */
    private void reloadPolicies(String reason) {
        log.info("Reloading policies: {}", reason);

        long startTime = System.currentTimeMillis();
        int oldPolicyCount = policyLoader.getCurrentPolicyCount(enforcer);

        // Reload policies from database
        policyLoader.loadPolicies(enforcer);

        int newPolicyCount = policyLoader.getCurrentPolicyCount(enforcer);
        long duration = System.currentTimeMillis() - startTime;

        log.info("Policy reload completed: {} → {} policies in {}ms ({})",
                oldPolicyCount, newPolicyCount, duration, reason);
    }

    /**
     * Handle reload failure and set up retry if needed
     */
    private void handleReloadFailure(Exception e, String context) {
        int currentRetries = retryCount.incrementAndGet();
        lastFailureTime.set(LocalDateTime.now());
        lastFailureReason.set(context + ": " + e.getMessage());

        if (currentRetries <= MAX_RETRY_COUNT) {
            log.warn("Policy reload failed (attempt {}/{}): {}. Will retry in {} minutes",
                    currentRetries, MAX_RETRY_COUNT, e.getMessage(), RETRY_INTERVAL_MINUTES);
        } else {
            log.error("Policy reload failed after {} attempts. Giving up retry mechanism. " +
                    "Manual intervention may be required", MAX_RETRY_COUNT);
        }
    }

    /**
     * Scheduled retry mechanism - runs every 5 minutes
     */
    @Scheduled(fixedRate = RETRY_INTERVAL_MINUTES * 60 * 1000) // 5 minutes in milliseconds
    public void scheduledRetry() {
        if (!shouldRetry()) {
            return;
        }

        LocalDateTime lastFailure = lastFailureTime.get();
        if (lastFailure == null) {
            return;
        }

        long minutesSinceFailure = ChronoUnit.MINUTES.between(lastFailure, LocalDateTime.now());
        if (minutesSinceFailure < RETRY_INTERVAL_MINUTES) {
            return; // Not time for retry yet
        }

        int currentRetries = retryCount.get();
        log.info("Attempting scheduled policy reload retry ({}/{})", currentRetries, MAX_RETRY_COUNT);

        try {
            reloadPolicies("Scheduled retry #" + currentRetries);
            resetRetryState();
            log.info("Scheduled policy reload retry succeeded");

        } catch (Exception e) {
            log.error("Scheduled policy reload retry failed", e);
            handleReloadFailure(e, "Scheduled retry failed");
        }
    }

    /**
     * Check if retry should be attempted
     */
    private boolean shouldRetry() {
        int currentRetries = retryCount.get();
        return currentRetries > 0 && currentRetries <= MAX_RETRY_COUNT;
    }

    /**
     * Reset retry state after successful reload
     */
    private void resetRetryState() {
        retryCount.set(0);
        lastFailureTime.set(null);
        lastFailureReason.set(null);
        log.debug("Retry state reset after successful policy reload");
    }

    /**
     * Get current retry status for monitoring
     */
    public RetryStatus getRetryStatus() {
        return RetryStatus.builder()
                .currentRetryCount(retryCount.get())
                .maxRetryCount(MAX_RETRY_COUNT)
                .lastFailureTime(lastFailureTime.get())
                .lastFailureReason(lastFailureReason.get())
                .isRetrying(shouldRetry())
                .build();
    }

    /**
     * Health check for policy update service
     */
    public boolean isHealthy() {
        // Consider unhealthy if we've exceeded max retries
        return retryCount.get() <= MAX_RETRY_COUNT;
    }

    /**
     * Retry status information for monitoring
     */
    @lombok.Builder
    @lombok.Data
    public static class RetryStatus {
        private int currentRetryCount;
        private int maxRetryCount;
        private LocalDateTime lastFailureTime;
        private String lastFailureReason;
        private boolean isRetrying;
    }
}