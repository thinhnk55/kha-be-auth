package com.defi.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "auth.session")
public class SessionConfig {
    private long durationInSeconds = 3600;
    private boolean userLimitActive = false;
    private long userSessionLimit = 10;
    private boolean ipLimitActive = false;
    private long ipSessionLimit = 600;
}
