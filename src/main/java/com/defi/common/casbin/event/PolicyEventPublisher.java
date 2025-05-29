package com.defi.common.casbin.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Publisher for policy reload events via Redis messaging.
 * 
 * <p>This service publishes policy change notifications to a Redis channel, enabling
 * distributed services to automatically reload their policy caches when permissions
 * are updated in the database.</p>
 * 
 * <p>Key features:</p>
 * <ul>
 *   <li>Publishes to a configurable Redis channel</li>
 *   <li>Sends standardized reload messages</li>
 *   <li>Graceful error handling to prevent breaking main operations</li>
 *   <li>Supports distributed policy synchronization</li>
 * </ul>
 * 
 * <p>Usage example:</p>
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

    /** Standard message sent to trigger policy reloads across services */
    private static final String RELOAD_MESSAGE = "RELOAD_POLICIES";
    
    /** Default Redis channel for policy change notifications */
    private static final String DEFAULT_CHANNEL = "casbin:policy:changes";

    /**
     * Publishes a policy reload event to the default Redis channel.
     * 
     * <p>This method notifies all listening services that policies have been updated
     * and they should reload their policy caches from the database.</p>
     * 
     * <p>The method is designed to be non-blocking and fail-safe - if the Redis
     * publish operation fails, it will log an error but not throw an exception
     * to avoid breaking the main business operation that triggered the policy change.</p>
     * 
     * @see #DEFAULT_CHANNEL
     * @see #RELOAD_MESSAGE
     */
    public void publishReloadEvent() {
        try {
            redisTemplate.convertAndSend(DEFAULT_CHANNEL, RELOAD_MESSAGE);
            log.info("Published policy reload event to channel: {}", DEFAULT_CHANNEL);
        } catch (Exception e) {
            log.error("Failed to publish policy reload event", e);
        }
    }
}