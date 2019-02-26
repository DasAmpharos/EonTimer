package io.github.dylmeadows.eontimer.model.settings

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.getValue
import tornadofx.setValue

class TimerSettingsModel {
    @Transient
    val consoleProperty = SimpleObjectProperty(DEFAULT_CONSOLE)
    var console by consoleProperty
    @Transient
    val refreshIntervalProperty = SimpleIntegerProperty(DEFAULT_REFRESH_INTERVAL)
    var refreshInterval by refreshIntervalProperty
    @Transient
    val precisionCalibrationModeProperty = SimpleBooleanProperty(DEFAULT_PRECISION_CALIBRATION_MODE)
    var precisionCalibrationMode by precisionCalibrationModeProperty
    @Transient
    val minimumLengthProperty = SimpleIntegerProperty(DEFAULT_MINIMUM_LENGTH)
    var minimumLength by minimumLengthProperty
}