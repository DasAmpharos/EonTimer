package com.github.dylmeadows;

import com.github.dylmeadows.common.javafx.beans.ModelSerializer;
import com.github.dylmeadows.common.util.ResourceBundles;
import com.github.dylmeadows.eontimer.EonTimerLogger;
import com.github.dylmeadows.eontimer.reference.resources.CssResource;
import com.github.dylmeadows.eontimer.ui.EonTimerController;
import com.github.dylmeadows.eontimer.ui.EonTimerModel;
import com.github.dylmeadows.eontimer.ui.EonTimerView;
import com.github.dylmeadows.eontimer.ui.settings.EonTimerSettingsModel;
import com.github.dylmeadows.eontimer.ui.timers.custom.CustomTimerController;
import com.github.dylmeadows.eontimer.ui.timers.custom.CustomTimerModel;
import com.github.dylmeadows.eontimer.ui.timers.custom.CustomTimerView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.Duration;
import java.util.ResourceBundle;

public class EonTimer extends Application {

    private EonTimerModel model;

    /**
     * Application strings.
     */
    private static final ResourceBundle res = ResourceBundles.getBundle(EonTimer.class);

    /**
     * Application name.
     */
    public static final String NAME = res.getString("name");

    /**
     * Application version.
     */
    public static final String VERSION = res.getString("version");

    @Override
    public void init() throws Exception {
        try {
            // initialize logger
            EonTimerLogger.getLogger().initialize();

            // load model
            model = loadModel();
        } catch (IOException e) {
            EonTimerLogger.getLogger().error(e);
            System.exit(-1);
        }
    }

    /**
     * JavaFX application entry-point. Initializes the GUI and models and shows the
     * application stage.
     *
     * @param stage application stage (frame)
     */
    @Override
    public void start(Stage stage) throws Exception {
        // initialize panel
        EonTimerView view = new EonTimerView();
        EonTimerController controller = new EonTimerController(model, view);

        // initialize scene
        Scene scene = new Scene(view);
        scene.getStylesheets().add(CssResource.EON_TIMER.getPath());

        // initialize and show the stage
        stage.setWidth(545);
        stage.setHeight(415);
        stage.setResizable(false);
        stage.setTitle(NAME + " v" + VERSION);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        try {
            persistModel(model);
        } catch (IOException e) {
            EonTimerLogger.getLogger().error(e);
            System.exit(-1);
        }
    }

    /**
     * Read settings from file if it exists. If settings file does not exist, create a new settings instance.
     *
     * @return application settings
     * @throws IOException see {@link Files#readAllBytes(Path)}
     */
    private EonTimerModel loadModel() throws IOException {
        File file = new File(NAME + ".json");
        if (file.exists()) {
            Path path = Paths.get(file.toURI());
            String json = new String(Files.readAllBytes(path));
            return ModelSerializer.fromJson(json, EonTimerModel.class);
        } else {
            return new EonTimerModel();
        }
    }

    /**
     * Writes settings to the settings file.
     *
     * @param model application settings
     * @throws IOException see {@link Files#write(Path, byte[], OpenOption...)}
     */
    private void persistModel(EonTimerModel model) throws IOException {
        File file = new File(NAME + ".json");
        Path path = Paths.get(file.toURI());
        String json = ModelSerializer.toJson(model);
        Files.write(path, json.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
