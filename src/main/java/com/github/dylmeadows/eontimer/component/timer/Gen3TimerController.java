package com.github.dylmeadows.eontimer.component.timer;

import com.github.dylmeadows.eontimer.model.Gen3TimerMode;
import com.github.dylmeadows.eontimer.model.Gen3TimerModel;
import com.github.dylmeadows.javafx.util.OptionConverter;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.VBox;
import moe.tristan.easyfxml.api.FxmlController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.github.dylmeadows.eontimer.model.Gen3TimerConstants.*;
import static moe.tristan.easyfxml.util.Nodes.hideAndResizeParentIf;

@Component
@SuppressWarnings({"unused", "WeakerAccess"})
public class Gen3TimerController implements FxmlController {

    private final Gen3TimerModel model;

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

    @Autowired
    public Gen3TimerController(Gen3TimerModel model) {
        this.model = model;
    }

    @Override
    public void initialize() {
        modeField.setItems(FXCollections.observableArrayList(Gen3TimerMode.values()));
        modeField.setConverter(OptionConverter.forOption(Gen3TimerMode.class));
        modeField.valueProperty().bindBidirectional(model.modeProperty());

        calibrationField.setValueFactory(createValueFactory(Integer.MIN_VALUE, Integer.MAX_VALUE, DEFAULT_CALIBRATION));
        model.calibrationProperty().asObject().bindBidirectional(calibrationField.getValueFactory().valueProperty());
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
