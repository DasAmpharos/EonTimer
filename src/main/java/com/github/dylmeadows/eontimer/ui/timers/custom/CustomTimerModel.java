package com.github.dylmeadows.eontimer.ui.timers.custom;

import com.github.dylmeadows.eontimer.ui.timers.TimerModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.Duration;

public class CustomTimerModel extends TimerModel {

    private final ObservableList<Duration> stages;

    public CustomTimerModel() {
        stages = FXCollections.observableArrayList();
    }

    public ObservableList<Duration> getStages() {
        return stages;
    }
}
