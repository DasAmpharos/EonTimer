package io.github.dylmeadows.eontimer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties("application")
public class AppProperties {
    private String name;
    private String version;
}
