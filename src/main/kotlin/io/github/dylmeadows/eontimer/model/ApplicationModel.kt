package io.github.dylmeadows.eontimer.model

import io.github.dylmeadows.eontimer.model.settings.ActionSettingsModel
import io.github.dylmeadows.eontimer.model.settings.TimerSettingsModel
import io.github.dylmeadows.eontimer.model.timer.CustomTimerModel
import io.github.dylmeadows.eontimer.model.timer.Gen3TimerModel
import io.github.dylmeadows.eontimer.model.timer.Gen4TimerModel
import io.github.dylmeadows.eontimer.model.timer.Gen5TimerModel
import io.github.dylmeadows.eontimer.model.timer.TimerConstants
import io.github.dylmeadows.eontimer.util.getValue
import io.github.dylmeadows.eontimer.util.setValue
import javafx.beans.property.SimpleObjectProperty

class ApplicationModel {
    val gen3 = Gen3TimerModel()
    val gen4 = Gen4TimerModel()
    val gen5 = Gen5TimerModel()
    val custom = CustomTimerModel()
    val actionSettings = ActionSettingsModel()
    val timerSettings = TimerSettingsModel()

    @Transient
    val selectedTimerTypeProperty = SimpleObjectProperty(TimerConstants.DEFAULT_TIMER_TYPE)
    var selectedTimerType by selectedTimerTypeProperty
}