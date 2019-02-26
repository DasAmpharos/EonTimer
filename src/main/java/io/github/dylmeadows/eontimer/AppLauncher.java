package io.github.dylmeadows.eontimer;

import io.github.dylmeadows.eontimer.model.resource.CssResource;
import io.github.dylmeadows.eontimer.model.resource.FxmlResource;
import io.github.dylmeadows.springboot.javafx.SpringJavaFxApplication;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@SpringBootApplication
public class AppLauncher extends SpringJavaFxApplication {

    @Override
    protected void onInit() {
        Stream.of("os.name", "os.version", "os.arch", "java.version", "java.vendor", "sun.arch.data.model")
            .collect(Collectors.toMap(Function.identity(), System::getProperty))
            .forEach((key, value) -> log.info("{} == {}", key, value));
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(load(FxmlResource.TimerMasterPane.get()));
        scene.getStylesheets().add(CssResource.MAIN.getPath());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(AppLauncher.class, args);
    }
}