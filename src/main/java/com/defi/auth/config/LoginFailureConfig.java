package com.defi.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "auth.login-failure")
public class LoginFailureConfig {
    private int loginFailureLimit = 5;
    private Duration lockedTime = Duration.ofMinutes(10);
    private Duration expire = Duration.ofHours(6);
}
