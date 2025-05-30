package com.defi.auth.permission.service;

import com.defi.common.casbin.event.PolicyEventConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class PolicyEventPublisher {

    private final RedisTemplate<String, String> redisTemplate;

    public void publishReloadPolicyEvent(long version) {
        String message = String.format("%s:%d", PolicyEventConstant.RELOAD_MESSAGE, version);
        try {
            redisTemplate.convertAndSend(PolicyEventConstant.DEFAULT_CHANNEL, message);
            log.info("Published custom policy reload event '{}' to channel: {}", message,
                    PolicyEventConstant.DEFAULT_CHANNEL);
        } catch (Exception e) {
            log.error("Failed to publish custom policy reload event: {}", message, e);
        }
    }
}