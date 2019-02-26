package io.github.dylmeadows.eontimer.model

import io.github.dylmeadows.eontimer.model.settings.ActionSettingsModel
import io.github.dylmeadows.eontimer.model.settings.TimerSettingsModel
import io.github.dylmeadows.eontimer.model.timer.*
import javafx.beans.property.SimpleObjectProperty
import tornadofx.getValue
import tornadofx.setValue

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