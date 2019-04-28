package io.github.dylmeadows.eontimer.model

import io.github.dylmeadows.eontimer.model.settings.ActionSettingsModel
import io.github.dylmeadows.eontimer.model.settings.TimerSettingsModel
import io.github.dylmeadows.eontimer.model.timer.*
import io.github.dylmeadows.eontimer.util.getValue
import io.github.dylmeadows.eontimer.util.setValue
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty

class ApplicationModel {
    var gen3 = Gen3TimerModel()
    var gen4 = Gen4TimerModel()
    var gen5 = Gen5TimerModel()
    var custom = CustomTimerModel()
    var actionSettings = ActionSettingsModel()
    var timerSettings = TimerSettingsModel()

    val selectedTimerTypeProperty: ObjectProperty<TimerType> = SimpleObjectProperty(TimerConstants.DEFAULT_TIMER_TYPE)
    var selectedTimerType: TimerType by selectedTimerTypeProperty
}