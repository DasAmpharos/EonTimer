package io.github.dylmeadows.eontimer.model.timer

import io.github.dylmeadows.eontimer.util.getValue
import io.github.dylmeadows.eontimer.util.setValue
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty

class Gen5TimerModel {
    val modeProperty =  SimpleObjectProperty<Gen5TimerMode>(Gen5TimerConstants.DEFAULT_MODE)
    var mode: Gen5TimerMode by modeProperty

    val calibrationProperty =  SimpleIntegerProperty(Gen5TimerConstants.DEFAULT_CALIBRATION)
    var calibration by calibrationProperty

    val targetDelayProperty =  SimpleIntegerProperty(Gen5TimerConstants.DEFAULT_TARGET_DELAY)
    var targetDelay by targetDelayProperty

    val targetSecondProperty =  SimpleIntegerProperty(Gen5TimerConstants.DEFAULT_TARGET_SECOND)
    var targetSecond by targetSecondProperty

    val entralinkCalibrationProperty =  SimpleIntegerProperty(Gen5TimerConstants.DEFAULT_ENTRALINK_CALIBRATION)
    var entralinkCalibration by entralinkCalibrationProperty

    val frameCalibrationProperty =  SimpleIntegerProperty(Gen5TimerConstants.DEFAULT_FRAME_CALIBRATION)
    var frameCalibration by frameCalibrationProperty

    val targetAdvancesProperty =  SimpleIntegerProperty(Gen5TimerConstants.DEFAULT_TARGET_ADVANCES)
    var targetAdvances by targetAdvancesProperty

    val secondHitProperty =  SimpleIntegerProperty()
    var secondHit by secondHitProperty

    val delayHitProperty =  SimpleIntegerProperty()
    var delayHit by delayHitProperty

    val actualAdvancesProperty =  SimpleIntegerProperty()
    var actualAdvances by actualAdvancesProperty
}