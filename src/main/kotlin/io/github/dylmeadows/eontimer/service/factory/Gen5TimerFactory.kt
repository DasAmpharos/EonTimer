package io.github.dylmeadows.eontimer.service.factory

import io.github.dylmeadows.eontimer.model.TimerModel
import io.github.dylmeadows.eontimer.model.settings.TimerSettingsConstants
import io.github.dylmeadows.eontimer.model.settings.TimerSettingsModel
import io.github.dylmeadows.eontimer.model.timer.Gen5TimerMode
import io.github.dylmeadows.eontimer.model.timer.Gen5TimerModel
import io.github.dylmeadows.eontimer.model.timer.TimerConstants
import io.github.dylmeadows.eontimer.util.Calibrations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class Gen5TimerFactory @Autowired constructor(
    private val timerModel: TimerModel,
    private val gen5TimerModel: Gen5TimerModel,
    private val timerSettings: TimerSettingsModel) : TimerFactory {

    override fun createTimer(): List<Long> {
        return when (gen5TimerModel.mode) {
            Gen5TimerMode.STANDARD -> createStandardTimer()
            else -> Collections.emptyList()
        }
    }

    private fun createStandardTimer(): List<Long> {
        return arrayOf(
            (gen5TimerModel.targetSecond * 1000 + gen5TimerModel.calibration + 200)
                .normalize().toLong(),
            TimerConstants.NULL_TIME_SPAN)
            .toList()
    }

    private fun calculateCalibration(): Int {
        return if (!timerSettings.precisionCalibrationMode)
            Calibrations.convertToMillis(gen5TimerModel.calibration, timerSettings.console)
        else
            gen5TimerModel.calibration
    }

    private fun calculateEntralinkCalibration(): Int {
        return if (!timerSettings.precisionCalibrationMode)
            Calibrations.convertToMillis(gen5TimerModel.entralinkCalibration, timerSettings.console)
        else
            gen5TimerModel.entralinkCalibration
    }

    private fun Int.normalize(): Int {
        var value = this
        while (this < TimerSettingsConstants.MINIMUM_LENGTH)
            value += 60000
        return value
    }
}