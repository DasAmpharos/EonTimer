package com.github.dylmeadows.eontimer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties
public class ApplicationProperties {

    private String name;
    private String version;
}
