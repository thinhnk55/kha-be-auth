package com.defi.auth.permission.service;

import com.defi.common.casbin.event.PolicyEventConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Publisher for policy reload events via Redis messaging.
 * 
 * <p>
 * This service publishes policy change notifications to a Redis channel,
 * enabling
 * distributed services to automatically reload their policy caches when
 * permissions
 * are updated in the database.
 * </p>
 * 
 * <p>
 * This service is restricted to the Auth module only, ensuring that only
 * the authorization service can trigger policy reload events across the system.
 * </p>
 * 
 * <p>
 * Key features:
 * </p>
 * <ul>
 * <li>Publishes to a configurable Redis channel</li>
 * <li>Sends standardized reload messages</li>
 * <li>Graceful error handling to prevent breaking main operations</li>
 * <li>Supports distributed policy synchronization</li>
 * <li>Auth module exclusive access control</li>
 * </ul>
 * 
 * <p>
 * Usage example:
 * </p>
 * 
 * <pre>
 * // After updating permissions in database
 * policyEventPublisher.publishReloadEvent();
 * // All services listening on the channel will reload their policies
 * </pre>
 * 
 * @author Defi Team
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PolicyEventPublisher {

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * Publishes a policy reload event to the default Redis channel.
     * 
     * <p>
     * This method notifies all listening services that policies have been updated
     * and they should reload their policy caches from the database.
     * </p>
     * 
     * <p>
     * The method is designed to be non-blocking and fail-safe - if the Redis
     * publish operation fails, it will log an error but not throw an exception
     * to avoid breaking the main business operation that triggered the policy
     * change.
     * </p>
     * 
     * <p>
     * <strong>Security Note:</strong> This method should only be called from within
     * the Auth service when policy changes occur (role assignments, permission
     * updates, etc.).
     * </p>
     * 
     * @see #DEFAULT_CHANNEL
     * @see #RELOAD_MESSAGE
     */
    public void publishReloadEvent() {
        try {
            redisTemplate.convertAndSend(PolicyEventConstant.DEFAULT_CHANNEL, PolicyEventConstant.RELOAD_MESSAGE);
            log.info("Published policy reload event to channel: {} from Auth service",
                    PolicyEventConstant.DEFAULT_CHANNEL);
        } catch (Exception e) {
            log.error("Failed to publish policy reload event from Auth service", e);
        }
    }

    /**
     * Publishes a policy reload event with custom message.
     * 
     * <p>
     * This method allows publishing custom reload messages for specific
     * scenarios or debugging purposes.
     * </p>
     * 
     * @param message custom message to publish
     */
    public void publishCustomReloadEvent(String message) {
        try {
            redisTemplate.convertAndSend(PolicyEventConstant.DEFAULT_CHANNEL, message);
            log.info("Published custom policy reload event '{}' to channel: {}", message,
                    PolicyEventConstant.DEFAULT_CHANNEL);
        } catch (Exception e) {
            log.error("Failed to publish custom policy reload event: {}", message, e);
        }
    }
}