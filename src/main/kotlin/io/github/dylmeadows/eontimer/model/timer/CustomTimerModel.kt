package io.github.dylmeadows.eontimer.model.timer

import javafx.collections.FXCollections

class CustomTimerModel {
    val stages: MutableList<Long> = FXCollections.observableArrayList<Long>()
}