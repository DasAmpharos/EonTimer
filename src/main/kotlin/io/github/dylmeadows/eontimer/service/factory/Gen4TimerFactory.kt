package io.github.dylmeadows.eontimer.service.factory

import io.github.dylmeadows.eontimer.model.TimerModel
import io.github.dylmeadows.eontimer.model.timer.Gen4TimerModel
import io.github.dylmeadows.eontimer.service.CalibrationService
import io.github.dylmeadows.eontimer.service.factory.timer.DelayTimer
import io.github.dylmeadows.eontimer.util.asFlux
import io.github.dylmeadows.eontimer.util.reactor.TimerState
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.time.Duration
import javax.annotation.PostConstruct

@Component
class Gen4TimerFactory @Autowired constructor(
    private val timerModel: TimerModel,
    private val gen4TimerModel: Gen4TimerModel,
    private val calibrationService: CalibrationService,
    private val delayTimer: DelayTimer) : TimerFactory {

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
                    timerModel.stages = stages
                }
            }
    }

    override val stages: List<Duration>
        get() = delayTimer.createStages(
            gen4TimerModel.targetSecond,
            gen4TimerModel.targetDelay,
            calibration)

    override fun createTimer(): Flux<TimerState> =
        delayTimer.createTimer(
            gen4TimerModel.targetSecond,
            gen4TimerModel.targetDelay,
            calibration)

    override fun calibrate() {
        gen4TimerModel.calibratedDelay += calibrationService.calibrate(
            delayTimer.calibrate(gen4TimerModel.targetDelay, gen4TimerModel.delayHit))
    }
}