package com.github.dylmeadows.eontimer;

import com.github.dylmeadows.eontimer.config.ApplicationProperties;
import lombok.extern.slf4j.Slf4j;
import moe.tristan.easyfxml.spring.application.FxSpringApplication;
import moe.tristan.easyfxml.spring.application.FxSpringContext;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import java.awt.*;
import java.util.stream.Stream;

@Slf4j
@SpringBootApplication
@Import({FxSpringContext.class})
public class EonTimerApplication extends FxSpringApplication {

    @Override
    protected void afterSpringInit() {
        ApplicationProperties properties = springContext.getBean(ApplicationProperties.class);
        log.info("{} v{}", properties.getName(), properties.getVersion());
        Stream.of("os.name", "os.version", "os.arch", "java.version", "java.vendor", "sun.arch.data.model")
                .forEach(property -> log.info("{} == {}", property, System.getProperty(property)));
    }

    public static void main(String[] args) {
        Toolkit.getDefaultToolkit();
        launch(args);
    }
}
