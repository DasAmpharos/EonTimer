package io.github.dylmeadows.eontimer.model.timer

import io.github.dylmeadows.eontimer.model.TimerStage
import javafx.collections.FXCollections
import javafx.collections.ObservableList

class CustomTimerModel {
    val stages: ObservableList<TimerStage> = FXCollections.observableArrayList<TimerStage>()
}