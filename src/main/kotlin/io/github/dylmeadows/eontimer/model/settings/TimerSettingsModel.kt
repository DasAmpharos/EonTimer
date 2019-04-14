package io.github.dylmeadows.eontimer.model.settings

import io.github.dylmeadows.eontimer.util.getValue
import io.github.dylmeadows.eontimer.util.setValue
import javafx.beans.property.BooleanProperty
import javafx.beans.property.LongProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleObjectProperty

class TimerSettingsModel {
    val consoleProperty: ObjectProperty<Console> = SimpleObjectProperty(TimerSettingsConstants.DEFAULT_CONSOLE)
    var console: Console by consoleProperty

    val refreshIntervalProperty: LongProperty = SimpleLongProperty(TimerSettingsConstants.DEFAULT_REFRESH_INTERVAL)
    var refreshInterval by refreshIntervalProperty

    val precisionCalibrationModeProperty: BooleanProperty = SimpleBooleanProperty(TimerSettingsConstants.DEFAULT_PRECISION_CALIBRATION_MODE)
    var precisionCalibrationMode by precisionCalibrationModeProperty
}