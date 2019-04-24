package io.github.dylmeadows.eontimer.model.timer

import io.github.dylmeadows.eontimer.util.getValue
import io.github.dylmeadows.eontimer.util.setValue
import javafx.beans.property.LongProperty
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleObjectProperty

class Gen5TimerModel {
    val modeProperty =  SimpleObjectProperty<Gen5TimerMode>(Gen5TimerConstants.DEFAULT_MODE)
    var mode: Gen5TimerMode by modeProperty

    val calibrationProperty: LongProperty =  SimpleLongProperty(Gen5TimerConstants.DEFAULT_CALIBRATION)
    var calibration by calibrationProperty

    val targetDelayProperty: LongProperty =  SimpleLongProperty(Gen5TimerConstants.DEFAULT_TARGET_DELAY)
    var targetDelay by targetDelayProperty

    val targetSecondProperty: LongProperty =  SimpleLongProperty(Gen5TimerConstants.DEFAULT_TARGET_SECOND)
    var targetSecond by targetSecondProperty

    val entralinkCalibrationProperty: LongProperty =  SimpleLongProperty(Gen5TimerConstants.DEFAULT_ENTRALINK_CALIBRATION)
    var entralinkCalibration by entralinkCalibrationProperty

    val frameCalibrationProperty: LongProperty =  SimpleLongProperty(Gen5TimerConstants.DEFAULT_FRAME_CALIBRATION)
    var frameCalibration by frameCalibrationProperty

    val targetAdvancesProperty: LongProperty =  SimpleLongProperty(Gen5TimerConstants.DEFAULT_TARGET_ADVANCES)
    var targetAdvances by targetAdvancesProperty

    val secondHitProperty: LongProperty =  SimpleLongProperty()
    var secondHit by secondHitProperty

    val delayHitProperty: LongProperty =  SimpleLongProperty()
    var delayHit by delayHitProperty

    val actualAdvancesProperty: LongProperty =  SimpleLongProperty()
    var actualAdvances by actualAdvancesProperty
}