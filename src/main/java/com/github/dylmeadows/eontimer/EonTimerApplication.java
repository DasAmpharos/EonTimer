package com.github.dylmeadows.eontimer;

import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;

@SpringBootApplication
public class EonTimerApplication extends Application {

    private ApplicationEntryPoint entry;
    private static ApplicationContext context;

    @Override
    public void init() throws Exception {
        entry = context.getBean(ApplicationEntryPoint.class);
        entry.init();
    }

    /**
     * JavaFX application entry-point. Initializes the GUI and models and shows the
     * application stage.
     *
     * @param stage application stage (frame)
     */
    @Override
    public void start(Stage stage) throws Exception {
        entry.start(stage);
        /*// initialize panel
//        EonTimerView view = new EonTimerView();
//        EonTimerController controller = new EonTimerController(model, view);

        // initialize scene
        String[] args = getParameters().getRaw().toArray(new String[0]);
        ApplicationContext ctx = new SpringApplicationBuilder().headless(false).run(args);

        Scene scene = new Scene(null);
        scene.getStylesheets().add(CssResource.EON_TIMER.getPath());

        // initialize and show the stage
        stage.setWidth(545);
        stage.setHeight(415);
        stage.setResizable(false);
        stage.setTitle(properties.getName() + " v" + properties.getVersion());
        stage.setScene(scene);
        stage.show();*/
    }


    @Override
    public void stop() throws Exception {
        entry.stop();
        SpringApplication.exit(context, () -> 0);
        /*
        try {
            persistModel(model);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            System.exit(-1);
        }*/
    }

    /*private EonTimerModel loadModel() throws IOException {
        File file = new File(properties.getName() + ".json");
        if (file.exists()) {
            Path path = Paths.get(file.toURI());
            String json = new String(Files.readAllBytes(path));
            return null;
        } else {
            return new EonTimerModel();
        }
    }*/

    /*private void persistModel(EonTimerModel model) throws IOException {
        File file = new File(properties.getName() + ".json");
        Path path = Paths.get(file.toURI());
//        String json = ModelSerializer.toJson(model);
//        Files.write(path, json.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }*/
    public static void main(String[] args) {
        context = SpringApplication.run(EonTimerApplication.class, args);
        launch(EonTimerApplication.class, args);
    }
}
