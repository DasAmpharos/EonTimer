package io.github.dylmeadows.eontimer.service.factory

import io.github.dylmeadows.eontimer.model.TimerModel
import io.github.dylmeadows.eontimer.model.settings.TimerSettingsModel
import io.github.dylmeadows.eontimer.model.timer.Gen4TimerModel
import io.github.dylmeadows.eontimer.service.CalibrationService
import io.github.dylmeadows.eontimer.service.factory.timer.DelayTimer
import io.github.dylmeadows.eontimer.util.asFlux
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class Gen4TimerFactory @Autowired constructor(
    private val timerModel: TimerModel,
    private val gen4TimerModel: Gen4TimerModel,
    private val calibrationService: CalibrationService,
    private val delayTimer: DelayTimer,
    timerSettings: TimerSettingsModel) : AbstractTimerFactory(timerSettings, calibrationService) {

    @PostConstruct
    private fun initialize() {
        listOf(
            gen4TimerModel.calibratedDelayProperty,
            gen4TimerModel.calibratedSecondProperty,
            gen4TimerModel.targetDelayProperty,
            gen4TimerModel.targetSecondProperty)
            .map { it.asFlux() }
            .forEach {
                it.subscribe {
                    timerModel.stages = createTimer()
                }
            }
    }

    override fun createTimer(): List<Long> {
        val calibration = calibrationService.createCalibration(gen4TimerModel.calibratedDelay, gen4TimerModel.calibratedSecond)
        return delayTimer.createStages(calibration, gen4TimerModel.targetSecond, gen4TimerModel.targetDelay)
    }

    override fun calibrate() {
        gen4TimerModel.calibratedDelay += delayTimer.calibrate(gen4TimerModel.targetDelay, gen4TimerModel.delayHit).calibrate()
    }
}