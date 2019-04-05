package io.github.dylmeadows.eontimer.service.factory

import io.github.dylmeadows.eontimer.model.settings.TimerSettingsModel
import io.github.dylmeadows.eontimer.service.CalibrationService

interface TimerFactory {

    fun createTimer(): List<Long>

    fun calibrate()
}

abstract class AbstractTimerFactory constructor(
    private val timerSettings: TimerSettingsModel,
    private val calibrationService: CalibrationService) : TimerFactory {

    fun Long.calibrate(): Long {
        return if (!timerSettings.precisionCalibrationMode)
            calibrationService.toDelays(this)
        else
            this
    }
}