package com.defi.common.casbin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Configuration properties for Casbin authorization framework.
 * 
 * <p>
 * This class holds essential configuration settings for Casbin policy
 * management,
 * including service identification, resource filtering, and Redis communication
 * settings.
 * </p>
 * 
 * <p>
 * Configuration properties are bound from application properties with prefix
 * {@code app.casbin}.
 * </p>
 * 
 * <p>
 * Example configuration:
 * </p>
 * 
 * <pre>
 * app.casbin.service-name=auth-service
 * app.casbin.resources=users,roles,permissions
 * app.casbin.enable-filtering=true
 * app.casbin.redis-channel=casbin:policy:changes
 * </pre>
 * 
 * @author Defi Team
 * @since 1.0.0
 */
@Component
@ConfigurationProperties(prefix = "app.casbin")
@Data
public class CasbinProperties {

    /**
     * List of resources this service manages or needs policies for.
     * When filtering is enabled, only policies for these resources will be loaded.
     * 
     * @default empty list (loads all policies)
     */
    private List<String> resources = List.of();
    private String policySource;
}