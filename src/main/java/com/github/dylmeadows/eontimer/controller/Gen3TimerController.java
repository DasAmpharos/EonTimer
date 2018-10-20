package com.github.dylmeadows.eontimer.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import org.apache.logging.log4j.util.TriConsumer;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import java.util.stream.Stream;

@Component
public class Gen3TimerController {

    @FXML
    private Spinner<Integer> calibration;
    @FXML
    private Spinner<Integer> preTimer;
    @FXML
    private Spinner<Integer> targetFrame;
    @FXML
    private Spinner<Integer> frameHit;

    public void initialize() {
        Stream.of(calibration, preTimer, targetFrame, frameHit)
                .forEach(this::setValueFactory);
    }

    private void setValueFactory(Spinner<Integer> spinner) {
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(Integer.MIN_VALUE, Integer.MAX_VALUE));
    }
}
