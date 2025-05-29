package com.defi.common.casbin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a policy rule for Casbin authorization.
 * Simplified model containing only essential fields for policy enforcement.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyRule {

    /**
     * Unique identifier of the permission
     */
    private Long id;

    /**
     * Role ID (subject in Casbin terms)
     */
    private Long roleId;

    /**
     * Resource code (object in Casbin terms)
     */
    private String resourceCode;

    /**
     * Action code (action in Casbin terms)
     */
    private String actionCode;

    /**
     * Convert to Casbin policy format: [subject, object, action]
     */
    public String[] toCasbinPolicy() {
        return new String[] {
                String.valueOf(roleId),
                resourceCode,
                actionCode
        };
    }

    /**
     * Create PolicyRule from database permission data
     */
    public static PolicyRule fromPermission(Long id, Long roleId, String resourceCode, String actionCode) {
        return PolicyRule.builder()
                .id(id)
                .roleId(roleId)
                .resourceCode(resourceCode)
                .actionCode(actionCode)
                .build();
    }

    /**
     * Check if this policy applies to a specific resource
     */
    public boolean appliesToResource(String targetResource) {
        return resourceCode != null && resourceCode.equals(targetResource);
    }

    /**
     * Get unique key for this policy (for deduplication)
     */
    public String getUniqueKey() {
        return String.format("%d:%s:%s", roleId, resourceCode, actionCode);
    }

    @Override
    public String toString() {
        return String.format("PolicyRule[%d: %d->%s:%s]",
                id, roleId, resourceCode, actionCode);
    }
}