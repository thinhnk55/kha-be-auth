package com.defi.common.casbin.event;

import com.defi.common.casbin.service.PolicyUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

/**
 * Simple Redis message listener for policy reload events.
 * Listens for "RELOAD_POLICIES" messages and triggers policy reload.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class PolicyEventListener implements MessageListener {

    private final PolicyUpdateService policyUpdateService;

    private static final String RELOAD_MESSAGE = "RELOAD_POLICIES";

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String channel = new String(pattern);
            String messageBody = new String(message.getBody());

            log.debug("Received message on channel: {} with content: {}", channel, messageBody);

            // Check if it's a reload message
            if (messageBody.startsWith(RELOAD_MESSAGE)) {
                String context = extractContext(messageBody);
                log.info("Processing policy reload event from channel: {} with context: {}", channel, context);

                // Trigger policy reload
                policyUpdateService.reloadPoliciesFromEvent(context);

            } else {
                log.debug("Ignoring unknown message: {}", messageBody);
            }

        } catch (Exception e) {
            log.error("Failed to process policy reload message", e);
            // Note: We don't re-throw here to avoid stopping the message listener
            // The PolicyUpdateService has its own retry mechanism
        }
    }

    /**
     * Extract context from message if available
     */
    private String extractContext(String messageBody) {
        if (messageBody.contains(":")) {
            return messageBody.substring(messageBody.indexOf(":") + 1);
        }
        return "Redis event";
    }
}