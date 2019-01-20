package com.github.dylmeadows.eontimer.controller.timer;

import com.github.dylmeadows.eontimer.model.timer.Gen3TimerConstants;
import com.github.dylmeadows.eontimer.model.timer.Gen3TimerMode;
import com.github.dylmeadows.eontimer.model.timer.Gen3TimerModel;
import com.github.dylmeadows.eontimer.app.util.SpinnerUtils;
import com.github.dylmeadows.javafx.util.OptionConverter;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import moe.tristan.easyfxml.api.FxmlController;
import moe.tristan.easyfxml.util.Nodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
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

    @Override
    public void initialize() {
        // Mode
        modeField.setItems(FXCollections.observableArrayList(Gen3TimerMode.values()));
        modeField.setConverter(OptionConverter.forOption(Gen3TimerMode.class));
        modeField.valueProperty().bindBidirectional(model.modeProperty());
        // Calibration
        SpinnerValueFactory<Integer> calibrationValueFactory = SpinnerUtils.createValueFactory(
                Integer.MIN_VALUE, Integer.MAX_VALUE, Gen3TimerConstants.DEFAULT_CALIBRATION);
        calibrationValueFactory.valueProperty().bindBidirectional(model.calibrationProperty().asObject());
        calibrationField.setValueFactory(calibrationValueFactory);
        // Pre-Timer
        SpinnerValueFactory<Integer> preTimerValueFactory = SpinnerUtils.createValueFactory(
                0, Integer.MAX_VALUE, Gen3TimerConstants.DEFAULT_PRE_TIMER);
        preTimerValueFactory.valueProperty().bindBidirectional(model.preTimerProperty().asObject());
        preTimerField.setValueFactory(preTimerValueFactory);
        // Target Frame
        SpinnerValueFactory<Integer> targetFrameValueFactory = SpinnerUtils.createValueFactory(
                0, Integer.MAX_VALUE, Gen3TimerConstants.DEFAULT_TARGET_FRAME);
        targetFrameValueFactory.valueProperty().bindBidirectional(model.targetFrameProperty().asObject());
        targetFrameField.setValueFactory(targetFrameValueFactory);
        // Frame Hit
        SpinnerValueFactory<Integer> frameHitValueFactory = SpinnerUtils.createValueFactory(0, Integer.MAX_VALUE);
        frameHitValueFactory.valueProperty().bindBidirectional(model.frameHitProperty().asObject());
        frameHitField.setValueFactory(frameHitValueFactory);

        final BooleanBinding isStandardMode = modeField.valueProperty().isEqualTo(Gen3TimerMode.STANDARD);
        Nodes.hideAndResizeParentIf(calibrationFieldSet, isStandardMode);
        Nodes.hideAndResizeParentIf(frameHitFieldSet, isStandardMode);
    }
}
