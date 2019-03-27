package io.github.dylmeadows.eontimer.model.timer

import io.github.dylmeadows.eontimer.util.getValue
import io.github.dylmeadows.eontimer.util.setValue
import javafx.beans.property.IntegerProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty

class Gen3TimerModel {
    val modeProperty: ObjectProperty<Gen3TimerMode> = SimpleObjectProperty(Gen3TimerConstants.DEFAULT_MODE)
    var mode: Gen3TimerMode by modeProperty

    val calibrationProperty: IntegerProperty = SimpleIntegerProperty(Gen3TimerConstants.DEFAULT_CALIBRATION)
    var calibration by calibrationProperty

    val preTimerProperty: IntegerProperty = SimpleIntegerProperty(Gen3TimerConstants.DEFAULT_PRE_TIMER)
    var preTimer by preTimerProperty

    val targetFrameProperty: IntegerProperty = SimpleIntegerProperty(Gen3TimerConstants.DEFAULT_TARGET_FRAME)
    var targetFrame by targetFrameProperty

    val frameHitProperty: IntegerProperty = SimpleIntegerProperty()
    var frameHit by frameHitProperty
}