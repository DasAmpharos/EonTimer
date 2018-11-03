package com.github.dylmeadows.eontimer.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Data;

import java.time.Duration;

@Data
public class CustomTimerModel {

    private final ObservableList<Long> stages = FXCollections.observableArrayList();
}
