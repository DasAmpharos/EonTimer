package io.github.dylmeadows.eontimer.controller.timer;

import io.github.dylmeadows.eontimer.model.timer.Gen4TimerMode;
import io.github.dylmeadows.eontimer.model.timer.Gen4TimerModel;
import io.github.dylmeadows.common.javafx.util.ChoiceConverter;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class Gen4TimerController {

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

    public void initialize() {
        // Mode
        modeField.setItems(FXCollections.observableArrayList(Gen4TimerMode.values()));
        modeField.setConverter(ChoiceConverter.forChoice(Gen4TimerMode.class));
        modeField.valueProperty().bindBidirectional(model.modeProperty());

    }
}
