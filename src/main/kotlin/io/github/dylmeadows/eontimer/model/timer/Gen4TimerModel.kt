package io.github.dylmeadows.eontimer.model.timer

import io.github.dylmeadows.eontimer.util.getValue
import io.github.dylmeadows.eontimer.util.setValue
import javafx.beans.property.LongProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleObjectProperty

class Gen4TimerModel {
    val modeProperty: ObjectProperty<Gen4TimerMode> = SimpleObjectProperty(Gen4TimerConstants.DEFAULT_MODE)
    var mode: Gen4TimerMode by modeProperty

    val calibratedDelayProperty: LongProperty = SimpleLongProperty(Gen4TimerConstants.DEFAULT_CALIBRATED_DELAY)
    var calibratedDelay by calibratedDelayProperty

    val calibratedSecondProperty: LongProperty = SimpleLongProperty(Gen4TimerConstants.DEFAULT_CALIBRATED_SECOND)
    var calibratedSecond by calibratedSecondProperty

    val targetDelayProperty: LongProperty = SimpleLongProperty(Gen4TimerConstants.DEFAULT_TARGET_DELAY)
    var targetDelay by targetDelayProperty

    val targetSecondProperty: LongProperty = SimpleLongProperty(Gen4TimerConstants.DEFAULT_TARGET_SECOND)
    var targetSecond by targetSecondProperty

    val delayHitProperty: LongProperty = SimpleLongProperty()
    var delayHit by delayHitProperty
}