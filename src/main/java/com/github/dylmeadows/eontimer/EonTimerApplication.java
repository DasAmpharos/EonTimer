package com.github.dylmeadows.eontimer;

import com.github.dylmeadows.eontimer.config.ApplicationProperties;
import com.github.dylmeadows.eontimer.core.TimerRunner;
import com.github.dylmeadows.eontimer.core.TimerState;
import com.github.dylmeadows.eontimer.model.Stage;
import com.github.dylmeadows.eontimer.model.Timer;
import com.github.dylmeadows.eontimer.model.TimerMasterModel;
import lombok.extern.slf4j.Slf4j;
import moe.tristan.easyfxml.spring.application.FxSpringApplication;
import moe.tristan.easyfxml.spring.application.FxSpringContext;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import java.awt.*;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@SpringBootApplication
@Import(FxSpringContext.class)
public class EonTimerApplication extends FxSpringApplication {

    @Override
    protected void afterSpringInit() {
        ApplicationProperties properties = springContext.getBean(ApplicationProperties.class);
        log.info("{} v{}", properties.getName(), properties.getVersion());
        Stream.of("os.name", "os.version", "os.arch", "java.version", "java.vendor", "sun.arch.data.model")
                .collect(Collectors.toMap(Function.identity(), System::getProperty))
                .forEach((key, value) -> log.info("{} == {}", key, value));
        TimerRunner runner = springContext.getBean(TimerRunner.class);
        TimerMasterModel timerModel = springContext.getBean(TimerMasterModel.class);
        timerModel.setTimer(new Timer(Arrays.asList(
            new Stage(10000), new Stage(10000)
        )));
        runner.start();
    }

    public static void main(String[] args) {
        Toolkit.getDefaultToolkit();
        launch(args);
    }
}
