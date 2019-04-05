package io.github.dylmeadows.eontimer.util

import io.github.dylmeadows.eontimer.model.TimerStage
import javafx.util.StringConverter

class TimerStageStringConverter : StringConverter<TimerStage>() {
    override fun toString(timerStage: TimerStage): String {
        return timerStage.length.toString()
    }

    override fun fromString(string: String): TimerStage {
        return TimerStage(string.toLong())
    }
}