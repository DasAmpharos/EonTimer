package com.github.dylmeadows.eontimer.component.timer;

import com.github.dylmeadows.eontimer.model.Gen3TimerMode;
import com.github.dylmeadows.javafx.util.OptionConverter;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.VBox;
import moe.tristan.easyfxml.api.FxmlController;
import org.springframework.stereotype.Component;

import static moe.tristan.easyfxml.util.Nodes.hideAndResizeParentIf;

@Component
@SuppressWarnings({"unused", "WeakerAccess"})
public class Gen3TimerController implements FxmlController {

    @FXML
    private ChoiceBox<Gen3TimerMode> modeField;
    @FXML
    private Spinner<Integer> calibrationField;
    @FXML
    private Spinner<Integer> preTimerField;
    @FXML
    private Spinner<Integer> targetFrameField;
    @FXML
    private Spinner<Integer> frameHitField;

    @FXML
    private VBox calibrationFieldSet;
    @FXML
    private VBox frameHitFieldSet;

    public static final Gen3TimerMode DEFAULT_MODE = Gen3TimerMode.STANDARD;
    public static final int DEFAULT_CALIBRATION = 0;
    public static final int DEFAULT_PRE_TIMER = 5000;
    public static final int DEFAULT_TARGET_FRAME = 1000;

    @Override
    public void initialize() {
        modeField.setItems(FXCollections.observableArrayList(Gen3TimerMode.values()));
        modeField.setConverter(OptionConverter.forOption(Gen3TimerMode.class));
        modeField.setValue(DEFAULT_MODE);

        calibrationField.setValueFactory(createValueFactory(Integer.MIN_VALUE, Integer.MAX_VALUE, DEFAULT_CALIBRATION));
        preTimerField.setValueFactory(createValueFactory(0, Integer.MAX_VALUE, DEFAULT_PRE_TIMER));
        targetFrameField.setValueFactory(createValueFactory(0, Integer.MAX_VALUE, DEFAULT_TARGET_FRAME));
        frameHitField.setValueFactory(createValueFactory(0, Integer.MAX_VALUE));

        final BooleanBinding isStandardMode = modeField.valueProperty().isEqualTo(Gen3TimerMode.STANDARD);
        hideAndResizeParentIf(calibrationFieldSet, isStandardMode);
        hideAndResizeParentIf(frameHitFieldSet, isStandardMode);
    }

    private SpinnerValueFactory<Integer> createValueFactory(int min, int max) {
        return new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max);
    }

    private SpinnerValueFactory<Integer> createValueFactory(int min, int max, int value) {
        return new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, value);
    }
}
