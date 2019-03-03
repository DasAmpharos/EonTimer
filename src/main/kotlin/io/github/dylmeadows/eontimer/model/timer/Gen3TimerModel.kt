package io.github.dylmeadows.eontimer.model.timer

import io.github.dylmeadows.eontimer.util.getValue
import io.github.dylmeadows.eontimer.util.setValue
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty

class Gen3TimerModel {
    val modeProperty = SimpleObjectProperty(Gen3TimerConstants.DEFAULT_MODE)
    var mode by modeProperty

    val calibrationProperty = SimpleIntegerProperty(Gen3TimerConstants.DEFAULT_CALIBRATION)
    var calibration by calibrationProperty

    val preTimerProperty = SimpleIntegerProperty(Gen3TimerConstants.DEFAULT_PRE_TIMER)
    var preTimer by preTimerProperty

    val targetFrameProperty = SimpleIntegerProperty(Gen3TimerConstants.DEFAULT_TARGET_FRAME)
    var targetFrame by targetFrameProperty

    val frameHitProperty = SimpleIntegerProperty()
    var frameHit by frameHitProperty
}