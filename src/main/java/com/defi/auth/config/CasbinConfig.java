package com.defi.auth.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.casbin.jcasbin.main.Enforcer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

@Configuration
@RequiredArgsConstructor
@Slf4j(topic = "Casbin")
public class CasbinConfig {

    private final ResourceLoader resourceLoader;

    @Bean
    public Enforcer casbinEnforcer() throws Exception {
        Resource model = resourceLoader.getResource("classpath:casbin/model.conf");
        Resource policy = resourceLoader.getResource("classpath:casbin/policy.csv");

        Enforcer enforcer = new Enforcer(
                model.getFile().getAbsolutePath(),
                policy.getFile().getAbsolutePath()
        );
        enforcer.enableLog(true);
        return enforcer;
    }
}
