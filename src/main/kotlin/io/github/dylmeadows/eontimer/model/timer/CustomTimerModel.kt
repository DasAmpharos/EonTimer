package io.github.dylmeadows.eontimer.model.timer

import io.github.dylmeadows.eontimer.model.Stage
import javafx.collections.FXCollections
import javafx.collections.ObservableList

class CustomTimerModel {
    val stages: ObservableList<Stage> = FXCollections.observableArrayList<Stage>()
}