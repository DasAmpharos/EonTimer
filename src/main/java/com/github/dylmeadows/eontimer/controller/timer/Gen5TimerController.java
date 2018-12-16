package com.github.dylmeadows.eontimer.controller.timer;

import com.github.dylmeadows.eontimer.model.timer.Gen5TimerConstants;
import com.github.dylmeadows.eontimer.model.timer.Gen5TimerMode;
import com.github.dylmeadows.eontimer.model.timer.Gen5TimerModel;
import com.github.dylmeadows.eontimer.util.SpinnerUtils;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SuppressWarnings({"unused", "WeakerAccess"})
public class Gen5TimerController implements FxmlController {

    private final Gen5TimerModel model;

    @FXML
    private ChoiceBox<Gen5TimerMode> modeField;
    @FXML
    private Spinner<Integer> calibrationField;
    @FXML
    private Spinner<Integer> targetDelayField;
    @FXML
    private Spinner<Integer> targetSecondField;
    @FXML
    private Spinner<Integer> entralinkCalibrationField;
    @FXML
    private Spinner<Integer> frameCalibrationField;
    @FXML
    private Spinner<Integer> targetAdvancesField;
    @FXML
    private Spinner<Integer> secondHitField;
    @FXML
    private Spinner<Integer> delayHitField;
    @FXML
    private Spinner<Integer> actualAdvancesField;

    @FXML
    private VBox modeFieldSet;
    @FXML
    private VBox calibrationFieldSet;
    @FXML
    private VBox targetDelayFieldSet;
    @FXML
    private VBox targetSecondFieldSet;
    @FXML
    private VBox entralinkCalibrationFieldSet;
    @FXML
    private VBox frameCalibrationFieldSet;
    @FXML
    private VBox targetAdvancesFieldSet;
    @FXML
    private VBox secondHitFieldSet;
    @FXML
    private VBox delayHitFieldSet;
    @FXML
    private VBox actualAdvancesFieldSet;

    @Override
    public void initialize() {
        // Mode
        modeField.setItems(FXCollections.observableArrayList(Gen5TimerMode.values()));
        modeField.setConverter(OptionConverter.forOption(Gen5TimerMode.class));
        modeField.valueProperty().bindBidirectional(model.modeProperty());
        // Calibration
        SpinnerValueFactory<Integer> calibrationValueFactory = SpinnerUtils.createValueFactory(
                Integer.MIN_VALUE, Integer.MAX_VALUE, Gen5TimerConstants.DEFAULT_CALIBRATION);
        calibrationValueFactory.valueProperty().bindBidirectional(model.calibrationProperty().asObject());
        calibrationField.setValueFactory(calibrationValueFactory);
        // Target Delay
        SpinnerValueFactory<Integer> targetDelayValueFactory = SpinnerUtils.createValueFactory(
                0, Integer.MAX_VALUE, Gen5TimerConstants.DEFAULT_TARGET_DELAY);
        targetDelayValueFactory.valueProperty().bindBidirectional(model.targetDelayProperty().asObject());
        targetDelayField.setValueFactory(targetDelayValueFactory);
        // Target Second
        SpinnerValueFactory<Integer> targetSecondValueFactory = SpinnerUtils.createValueFactory(
                0, Integer.MAX_VALUE, Gen5TimerConstants.DEFAULT_TARGET_SECOND);
        targetSecondValueFactory.valueProperty().bindBidirectional(model.targetSecondProperty().asObject());
        targetSecondField.setValueFactory(targetSecondValueFactory);
        // Entralink Calibration
        SpinnerValueFactory<Integer> entralinkCalibrationValueFactory = SpinnerUtils.createValueFactory(
                0, Integer.MAX_VALUE, Gen5TimerConstants.DEFAULT_ENTRALINK_CALIBRATION);
        entralinkCalibrationValueFactory.valueProperty().bindBidirectional(model.entralinkCalibrationProperty().asObject());
        entralinkCalibrationField.setValueFactory(entralinkCalibrationValueFactory);
        // Frame Calibration
        SpinnerValueFactory<Integer> frameCalibrationValueFactory = SpinnerUtils.createValueFactory(
                Integer.MIN_VALUE, Integer.MAX_VALUE, Gen5TimerConstants.DEFAULT_FRAME_CALIBRATION);
        frameCalibrationValueFactory.valueProperty().bindBidirectional(model.frameCalibrationProperty().asObject());
        frameCalibrationField.setValueFactory(frameCalibrationValueFactory);
        // Target Advances
        SpinnerValueFactory<Integer> targetAdvancesValueFactory = SpinnerUtils.createValueFactory(
                0, Integer.MAX_VALUE, Gen5TimerConstants.DEFAULT_TARGET_ADVANCES);
        targetAdvancesValueFactory.valueProperty().bindBidirectional(model.targetAdvancesProperty().asObject());
        targetAdvancesField.setValueFactory(targetAdvancesValueFactory);
        // Second Hit
        SpinnerValueFactory<Integer> secondHitValueFactory = SpinnerUtils.createValueFactory(
                0, Integer.MAX_VALUE);
        secondHitValueFactory.valueProperty().bindBidirectional(model.secondHitProperty().asObject());
        secondHitField.setValueFactory(secondHitValueFactory);
        // Delay Hit
        SpinnerValueFactory<Integer> delayHitValueFactory = SpinnerUtils.createValueFactory(
                0, Integer.MAX_VALUE);
        delayHitValueFactory.valueProperty().bindBidirectional(model.delayHitProperty().asObject());
        delayHitField.setValueFactory(delayHitValueFactory);
        // Actual Advances
        SpinnerValueFactory<Integer> actualAdvancesValueFactory = SpinnerUtils.createValueFactory(
                0, Integer.MAX_VALUE);
        actualAdvancesValueFactory.valueProperty().bindBidirectional(model.actualAdvancesProperty().asObject());
        actualAdvancesField.setValueFactory(actualAdvancesValueFactory);
    }
}
