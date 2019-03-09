package io.github.dylmeadows.eontimer.model.timer

import io.github.dylmeadows.eontimer.util.getValue
import io.github.dylmeadows.eontimer.util.setValue
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty

class Gen4TimerModel {
    val modeProperty = SimpleObjectProperty(Gen4TimerConstants.DEFAULT_MODE)
    var mode: Gen4TimerMode by modeProperty

    val calibratedDelayProperty = SimpleIntegerProperty(Gen4TimerConstants.DEFAULT_CALIBRATED_DELAY)
    var calibratedDelay by calibratedDelayProperty

    val calibratedSecondProperty = SimpleIntegerProperty(Gen4TimerConstants.DEFAULT_CALIBRATED_SECOND)
    var calibratedSecond by calibratedSecondProperty

    val targetDelayProperty = SimpleIntegerProperty(Gen4TimerConstants.DEFAULT_TARGET_DELAY)
    var targetDelay by targetDelayProperty

    val targetSecondProperty = SimpleIntegerProperty(Gen4TimerConstants.DEFAULT_TARGET_SECOND)
    var targetSecond by targetSecondProperty

    val delayHitProperty = SimpleIntegerProperty()
    var delayHit by delayHitProperty
}