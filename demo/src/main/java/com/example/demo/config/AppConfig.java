package com.example.demo.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
public class AppConfig {

    @Autowired
    private ExternalConfigProperties externalConfigProperties;

    @Bean
    public Resource[] externalConfigResources(ResourcePatternResolver resourcePatternResolver) throws IOException {
        String location = externalConfigProperties.getLocation() + "*.properties";
        return ResourcePatternUtils.getResourcePatternResolver(resourcePatternResolver).getResources(location);
    }
}
