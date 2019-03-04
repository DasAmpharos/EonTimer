package io.github.dylmeadows.eontimer.service.factory

import io.github.dylmeadows.eontimer.model.settings.TimerSettingsModel
import io.github.dylmeadows.eontimer.model.timer.Gen3TimerMode
import io.github.dylmeadows.eontimer.model.timer.Gen3TimerModel
import io.github.dylmeadows.eontimer.util.Calibrations.convertToMillis
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class Gen3TimerFactory @Autowired constructor(
    private val timerModel: Gen3TimerModel,
    private val timerSettings: TimerSettingsModel) : TimerFactory {

    private val stages: LongArray
        get() {
            return longArrayOf(
                createStage1(),
                createStage2())
        }

    override fun createTimer(): List<Long> {
        return when (timerModel.mode) {
            Gen3TimerMode.STANDARD -> stages.toList()
            Gen3TimerMode.VARIABLE_TARGET -> emptyList()
            else -> emptyList()
        }
    }

    private fun createStage1(): Long {
        return timerModel.preTimer.toLong()
    }

    private fun createStage2(): Long {
        return convertToMillis(timerModel.targetFrame, timerSettings.console) + timerModel.calibration
    }

}