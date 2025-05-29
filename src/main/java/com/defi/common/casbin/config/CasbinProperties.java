package com.defi.common.casbin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Configuration properties for Casbin authorization.
 * Simplified for database-only policy loading with event-driven updates.
 */
@Component
@ConfigurationProperties(prefix = "app.casbin")
@Data
public class CasbinProperties {

    /**
     * Service name for identification
     */
    private String serviceName = "unknown-service";

    /**
     * List of resources this service manages/needs policies for
     */
    private List<String> resources = List.of();

    /**
     * Enable policy filtering by service resources
     * If false, loads all policies regardless of resources
     */
    private boolean enableFiltering = true;

    /**
     * Redis channel for policy change notifications
     */
    private String redisChannel = "casbin:policy:changes";

    /**
     * Enable automatic policy reloading when events are received
     */
    private boolean autoReload = true;

    /**
     * Maximum retry count for failed policy loads
     */
    private int retryCount = 3;

    /**
     * Retry interval in minutes for failed policy loads
     */
    private int retryIntervalMinutes = 5;

    /**
     * Check if service has resources configured
     */
    public boolean hasResources() {
        return resources != null && !resources.isEmpty();
    }

    /**
     * Check if a resource is managed by this service
     */
    public boolean managesResource(String resourceCode) {
        return hasResources() && resources.contains(resourceCode);
    }

    /**
     * Check if any of the provided resources are managed by this service
     */
    public boolean managesAnyResource(List<String> resourceCodes) {
        if (!hasResources() || resourceCodes == null || resourceCodes.isEmpty()) {
            return false;
        }

        return resourceCodes.stream().anyMatch(this::managesResource);
    }
}