package com.github.dylmeadows.eontimer;

import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import moe.tristan.easyfxml.spring.application.FxSpringApplication;
import moe.tristan.easyfxml.spring.application.FxSpringContext;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@SpringBootApplication
@Import(FxSpringContext.class)
public class AppLauncher extends FxSpringApplication {

    @Override
    protected void afterSpringInit() {
        AppProperties properties = springContext.getBean(AppProperties.class);
        log.info("{} v{}", properties.getName(), properties.getVersion());
        Stream.of("os.name", "os.version", "os.arch", "java.version", "java.vendor", "sun.arch.data.model")
            .collect(Collectors.toMap(Function.identity(), System::getProperty))
            .forEach((key, value) -> log.info("{} == {}", key, value));
    }

    @Override
    public void start(Stage stage) {
        // TODO: set main scene
        stage.show();
    }

    public static void main(String[] args) {
        launch(AppLauncher.class, args);
    }
}
