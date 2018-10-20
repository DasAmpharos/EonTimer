package com.github.dylmeadows.eontimer;

import com.github.dylmeadows.eontimer.config.ApplicationProperties;
import com.github.dylmeadows.eontimer.model.resources.FxmlResource;
import com.github.dylmeadows.eontimer.util.aspect.Log;
import com.github.dylmeadows.eontimer.util.aspect.MetricType;
import com.github.dylmeadows.eontimer.util.aspect.Metrics;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.stream.Stream;

@Slf4j
@Component
public class EonTimerEntryPoint implements ApplicationEntryPoint {

    private ApplicationContext context;
    private ApplicationProperties properties;

    @Autowired
    public EonTimerEntryPoint(
            ApplicationContext context,
            ApplicationProperties properties) {
        this.context = context;
        this.properties = properties;
    }

    @Log
    @Metrics(MetricType.ELAPSED_MS)
    @Override
    public void init() {
        log.info("{} v{}", properties.getName(), properties.getVersion());
        Stream.of("os.name", "os.version", "os.arch", "java.version", "java.vendor", "sun.arch.data.model")
                .forEach(property -> log.info("{} == {}", property, System.getProperty(property)));
    }

    @Log
    @Metrics(MetricType.ELAPSED_MS)
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(context::getBean);
        Parent view = loader.load(FxmlResource.GEN_3_TIMER_VIEW.getAsStream());
        stage.setScene(new Scene(view));
        stage.show();
    }

    @Log
    @Metrics(MetricType.ELAPSED_MS)
    @Override
    public void stop() {
    }
}
