package com.defi.common.config;

import com.defi.common.web.IpAddressArgumentResolver;
import com.defi.common.web.UserAgentArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;


@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private IpAddressArgumentResolver ipAddressArgumentResolver;

    @Autowired
    private UserAgentArgumentResolver userAgentArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(ipAddressArgumentResolver);
        resolvers.add(userAgentArgumentResolver);
    }
}