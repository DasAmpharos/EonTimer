package io.github.dylmeadows.eontimer.service

import io.github.dylmeadows.eontimer.model.settings.TimerSettingsModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CalibrationService @Autowired constructor(
    private val timerSettingsModel: TimerSettingsModel) {

    private val console get() = timerSettingsModel.console

    fun toMillis(delays: Long): Long =
        Math.round(delays * console.frameRate)

    fun toDelays(millis: Long): Long =
        Math.round(millis / console.frameRate)

    fun createCalibration(delay: Long, second: Long): Long =
        toMillis(delay - toDelays(second * 1000))

    fun calibrate(value: Long): Long {
        return if (!timerSettingsModel.precisionCalibrationMode)
            toMillis(value)
        else
            value
    }
}