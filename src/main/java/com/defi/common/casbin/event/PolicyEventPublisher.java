package com.defi.common.casbin.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Simple publisher for policy reload events.
 * Sends a single "RELOAD_POLICIES" message when policies change.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PolicyEventPublisher {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String RELOAD_MESSAGE = "RELOAD_POLICIES";
    private static final String DEFAULT_CHANNEL = "casbin:policy:changes";

    /**
     * Publish policy reload event to default channel
     */
    public void publishReloadEvent() {
        publishReloadEvent(DEFAULT_CHANNEL);
    }

    /**
     * Publish policy reload event to specific channel
     */
    public void publishReloadEvent(String channel) {
        try {
            redisTemplate.convertAndSend(channel, RELOAD_MESSAGE);
            log.info("Published policy reload event to channel: {}", channel);
        } catch (Exception e) {
            log.error("Failed to publish policy reload event to channel: {}", channel, e);
            // Don't re-throw - publishing failure shouldn't break the main operation
        }
    }

    /**
     * Publish with context message for debugging
     */
    public void publishReloadEvent(String channel, String context) {
        try {
            String message = RELOAD_MESSAGE + ":" + context;
            redisTemplate.convertAndSend(channel, message);
            log.info("Published policy reload event to channel: {} with context: {}", channel, context);
        } catch (Exception e) {
            log.error("Failed to publish policy reload event to channel: {} with context: {}",
                    channel, context, e);
        }
    }
}