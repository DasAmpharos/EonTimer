package io.github.dylmeadows.eontimer.service.factory

import io.github.dylmeadows.eontimer.model.TimerModel
import io.github.dylmeadows.eontimer.model.settings.TimerSettingsConstants
import io.github.dylmeadows.eontimer.model.settings.TimerSettingsModel
import io.github.dylmeadows.eontimer.model.timer.Gen4TimerMode
import io.github.dylmeadows.eontimer.model.timer.Gen4TimerModel
import io.github.dylmeadows.eontimer.util.Calibrations.convertToMillis
import io.github.dylmeadows.eontimer.util.Calibrations.createCalibration
import io.github.dylmeadows.eontimer.util.asFlux
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class Gen4TimerFactory @Autowired constructor(
    private val timerModel: TimerModel,
    private val gen4TimerModel: Gen4TimerModel,
    private val timerSettings: TimerSettingsModel) : TimerFactory {

    @PostConstruct
    private fun initialize() {
        gen4TimerModel.calibratedDelayProperty.asFlux()
            .subscribe { timerModel.stages = createTimer() }
        gen4TimerModel.calibratedSecondProperty.asFlux()
            .subscribe { timerModel.stages = createTimer() }
        gen4TimerModel.targetDelayProperty.asFlux()
            .subscribe { timerModel.stages = createTimer() }
        gen4TimerModel.targetSecondProperty.asFlux()
            .subscribe { timerModel.stages = createTimer() }
    }

    override fun createTimer(): List<Long> {
        return when (gen4TimerModel.mode) {
            Gen4TimerMode.STANDARD -> {
                val calibration = createCalibration(
                    gen4TimerModel.calibratedDelay,
                    gen4TimerModel.calibratedSecond,
                    timerSettings.console)
                longArrayOf(
                    createStage1(calibration),
                    createStage2(calibration))
                    .toList()
            }
        }
    }

    private fun createStage1(calibration: Long): Long {
        val a = gen4TimerModel.targetSecond * 1000 + calibration + 200
        val b = convertToMillis(gen4TimerModel.targetDelay, timerSettings.console)
        return normalize(normalize(a) - b)
    }

    private fun createStage2(calibration: Long): Long {
        return convertToMillis(gen4TimerModel.targetDelay, timerSettings.console) - calibration
    }

    private fun normalize(value: Long): Long {
        var normalized = value
        while (normalized < TimerSettingsConstants.MINIMUM_LENGTH)
            normalized += 60000
        return normalized
    }

}