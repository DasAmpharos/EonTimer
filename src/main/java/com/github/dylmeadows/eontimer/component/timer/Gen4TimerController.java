package com.github.dylmeadows.eontimer.component.timer;

import com.github.dylmeadows.eontimer.model.Gen4TimerMode;
import com.github.dylmeadows.javafx.util.OptionConverter;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import moe.tristan.easyfxml.api.FxmlController;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings({"unused", "WeakerAccess"})
public class Gen4TimerController implements FxmlController {

    @FXML
    private ChoiceBox<Gen4TimerMode> modeField;
    @FXML
    private Spinner<Integer> calibratedDelayField;
    @FXML
    private Spinner<Integer> calibratedSecondField;
    @FXML
    private Spinner<Integer> targetDelayField;
    @FXML
    private Spinner<Integer> targetSecondField;
    @FXML
    private Spinner<Integer> delayHitField;

    public static final Gen4TimerMode DEFAULT_MODE = Gen4TimerMode.STANDARD;
    public static final int DEFAULT_CALIBRATED_DELAY = 500;
    public static final int DEFAULT_CALIBRATED_SECOND = 14;
    public static final int DEFAULT_TARGET_DELAY = 600;
    public static final int DEFAULT_TARGET_SECOND = 50;

    @Override
    public void initialize() {
        modeField.setItems(FXCollections.observableArrayList(Gen4TimerMode.values()));
        modeField.setConverter(OptionConverter.forOption(Gen4TimerMode.class));
        modeField.setValue(DEFAULT_MODE);
    }
}
