package io.github.dylmeadows.eontimer.model.timer

import javafx.collections.FXCollections
import javafx.collections.ObservableList

class CustomTimerModel {
    val stages: ObservableList<Long> = FXCollections.observableArrayList<Long>()
}