package io.github.dylmeadows.eontimer.service.factory

import io.github.dylmeadows.eontimer.model.TimerState
import io.github.dylmeadows.eontimer.model.timer.Gen4TimerModel
import io.github.dylmeadows.eontimer.service.CalibrationService
import io.github.dylmeadows.eontimer.service.factory.timer.DelayTimerFactory
import io.github.dylmeadows.eontimer.util.asFlux
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.Duration
import javax.annotation.PostConstruct

@Component
class Gen4TimerFactory @Autowired constructor(
    private val timerState: TimerState,
    private val gen4TimerModel: Gen4TimerModel,
    private val delayTimerFactory: DelayTimerFactory,
    private val calibrationService: CalibrationService) : TimerFactory {

    private val calibration: Long
        get() = calibrationService.createCalibration(gen4TimerModel.calibratedDelay, gen4TimerModel.calibratedSecond)

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
                    timerState.update(stages)
                }
            }
    }

    override val stages: List<Duration>
        get() = delayTimerFactory.createStages(
            gen4TimerModel.targetSecond,
            gen4TimerModel.targetDelay,
            calibration)

    override fun calibrate() {
        gen4TimerModel.calibratedDelay +=
            calibrationService.calibrateToDelays(
                delayTimerFactory.calibrate(
                    gen4TimerModel.targetDelay,
                    gen4TimerModel.delayHit))
    }
}