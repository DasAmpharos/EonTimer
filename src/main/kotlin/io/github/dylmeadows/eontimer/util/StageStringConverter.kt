package io.github.dylmeadows.eontimer.util

import io.github.dylmeadows.eontimer.model.Stage
import javafx.util.StringConverter

class StageStringConverter : StringConverter<Stage>() {
    override fun toString(timerStage: Stage): String {
        return timerStage.length.toString()
    }

    override fun fromString(string: String): Stage {
        return Stage(string.toLong())
    }
}