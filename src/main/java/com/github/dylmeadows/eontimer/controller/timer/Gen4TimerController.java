package com.github.dylmeadows.eontimer.controller.timer;

import com.github.dylmeadows.eontimer.model.timer.Gen4TimerConstants;
import com.github.dylmeadows.eontimer.model.timer.Gen4TimerMode;
import com.github.dylmeadows.eontimer.model.timer.Gen4TimerModel;
import com.github.dylmeadows.javafx.util.OptionConverter;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import moe.tristan.easyfxml.api.FxmlController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings({"unused", "WeakerAccess"})
public class Gen4TimerController implements FxmlController {

    private final Gen4TimerModel model;

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

    @Autowired
    public Gen4TimerController(final Gen4TimerModel model) {
        this.model = model;
    }

    @Override
    public void initialize() {
        modeField.setItems(FXCollections.observableArrayList(Gen4TimerMode.values()));
        modeField.setConverter(OptionConverter.forOption(Gen4TimerMode.class));
        modeField.valueProperty().bindBidirectional(model.modeProperty());
    }
}
