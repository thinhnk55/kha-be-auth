package com.defi.auth.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.casbin.jcasbin.main.Enforcer;
import org.casbin.jcasbin.model.Model;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.nio.charset.StandardCharsets;

@Configuration
@RequiredArgsConstructor
@Slf4j(topic = "Casbin")
public class CasbinConfig {

    private final ResourceLoader resourceLoader;

    @Bean
    public Enforcer casbinEnforcer() throws Exception {
        Resource modelResource = resourceLoader.getResource("classpath:casbin/model.conf");
        String modelText = new String(modelResource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        Model model = new Model();
        model.loadModelFromText(modelText);
        Enforcer enforcer = new Enforcer(model);
        return enforcer;
    }
}
