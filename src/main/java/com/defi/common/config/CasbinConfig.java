package com.defi.common.config;

import com.defi.common.casbin.config.CasbinProperties;
import com.defi.common.casbin.event.PolicyEventListener;
import com.defi.common.casbin.service.PolicyLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.casbin.jcasbin.main.Enforcer;
import org.casbin.jcasbin.model.Model;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Casbin configuration for simplified architecture.
 * Loads policies from database only, with event-driven updates.
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
@EnableAsync
@EnableScheduling
public class CasbinConfig {

    private final CasbinProperties casbinProperties;
    private final PolicyLoader policyLoader;

    /**
     * Create Casbin Enforcer bean
     */
    @Bean
    public Enforcer enforcer() {
        try {
            // Load model from classpath
            ClassPathResource modelResource = new ClassPathResource("casbin/rbac_model.conf");
            Model model = Enforcer.newModel(modelResource.getPath());

            // Create enforcer without initial policies
            Enforcer enforcer = new Enforcer(model);

            log.info("Casbin enforcer created successfully for service: {}",
                    casbinProperties.getServiceName());

            return enforcer;

        } catch (Exception e) {
            log.error("Failed to create Casbin enforcer", e);
            throw new RuntimeException("Casbin enforcer creation failed", e);
        }
    }

    /**
     * Load initial policies after application startup
     */
    @EventListener(ApplicationReadyEvent.class)
    public void loadInitialPolicies() {
        try {
            log.info("Loading initial policies for service: {}", casbinProperties.getServiceName());

            Enforcer enforcer = enforcer();
            policyLoader.loadPolicies(enforcer);

            log.info("Initial policy loading completed successfully");

        } catch (Exception e) {
            log.error("Failed to load initial policies", e);
            throw new RuntimeException("Initial policy loading failed", e);
        }
    }

    /**
     * Redis message listener container for policy change events
     */
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory,
            PolicyEventListener policyEventListener) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        // Subscribe to policy change events
        String channel = casbinProperties.getRedisChannel();
        container.addMessageListener(policyEventListener, new ChannelTopic(channel));

        log.info("Redis message listener configured for channel: {}", channel);
        return container;
    }
}
