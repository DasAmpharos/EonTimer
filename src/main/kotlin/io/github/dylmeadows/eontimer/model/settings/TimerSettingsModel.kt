package io.github.dylmeadows.eontimer.model.settings

import io.github.dylmeadows.eontimer.util.getValue
import io.github.dylmeadows.eontimer.util.setValue
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty

class TimerSettingsModel {
    val consoleProperty = SimpleObjectProperty(TimerSettingsConstants.DEFAULT_CONSOLE)
    var console by consoleProperty

    val refreshIntervalProperty = SimpleIntegerProperty(TimerSettingsConstants.DEFAULT_REFRESH_INTERVAL)
    var refreshInterval by refreshIntervalProperty

    val precisionCalibrationModeProperty = SimpleBooleanProperty(TimerSettingsConstants.DEFAULT_PRECISION_CALIBRATION_MODE)
    var precisionCalibrationMode by precisionCalibrationModeProperty

    val minimumLengthProperty = SimpleIntegerProperty(TimerSettingsConstants.DEFAULT_MINIMUM_LENGTH)
    var minimumLength by minimumLengthProperty
}