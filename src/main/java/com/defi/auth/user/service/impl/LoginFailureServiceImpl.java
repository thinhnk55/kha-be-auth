package com.defi.auth.user.service.impl;

import com.defi.auth.config.LoginFailureConfig;
import com.defi.auth.user.service.LoginFailureService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class LoginFailureServiceImpl implements LoginFailureService {
    private static final String LOGIN_FAILURE_PREFIX = "login-failure:";
    private final RedisTemplate<String, Integer> redisTemplate;
    private final LoginFailureConfig config;

    @Override
    public long onFailure(String username) {
        String key = LOGIN_FAILURE_PREFIX + username;
        ValueOperations<String, Integer> ops = redisTemplate.opsForValue();

        // Tăng số lần fail (INCR), tự động set = 1 nếu key chưa tồn tại
        int failureCount = ops.increment(key).intValue();

        // Reset TTL mỗi lần fail
        redisTemplate.expire(key, Duration.ofSeconds(config.getExpire().toSeconds()));

        if (failureCount > config.getLoginFailureLimit()) {
            return (long) failureCount * failureCount * config.getLockedTime().toSeconds();
        }
        return 0;
    }

    @Override
    public void onSuccess(String username) {
        String key = LOGIN_FAILURE_PREFIX + username;
        redisTemplate.delete(key);
    }
}
