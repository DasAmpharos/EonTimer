package io.github.dylmeadows.eontimer.service.factory

import io.github.dylmeadows.eontimer.model.settings.TimerSettingsModel
import io.github.dylmeadows.eontimer.model.timer.Gen4TimerMode
import io.github.dylmeadows.eontimer.model.timer.Gen4TimerModel
import io.github.dylmeadows.eontimer.util.Calibrations.convertToMillis
import io.github.dylmeadows.eontimer.util.Calibrations.createCalibration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class Gen4TimerFactory @Autowired constructor(
    private val timerModel: Gen4TimerModel,
    private val timerSettings: TimerSettingsModel) : TimerFactory {

    private val stages: LongArray
        get() {
            val calibration = createCalibration(
                timerModel.calibratedDelay,
                timerModel.calibratedSecond,
                timerSettings.console)
            return longArrayOf(
                createStage1(calibration),
                createStage2(calibration))
        }

    override fun createTimer(): List<Long> {
        return when (timerModel.mode) {
            Gen4TimerMode.STANDARD -> stages.toList()
            else -> emptyList()
        }
    }

    private fun createStage1(calibration: Long): Long {
        val a = timerModel.targetSecond * 1000 + calibration + 200
        val b = convertToMillis(timerModel.targetDelay, timerSettings.console)
        return normalize(normalize(a) - b)
    }

    private fun createStage2(calibration: Long): Long {
        return convertToMillis(timerModel.targetDelay, timerSettings.console) - calibration
    }

    private fun normalize(value: Long): Long {
        var normalized = value
        while (normalized < timerSettings.minimumLength)
            normalized += 60000
        return normalized
    }

}