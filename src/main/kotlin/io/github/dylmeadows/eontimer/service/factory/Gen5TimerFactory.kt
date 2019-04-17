package io.github.dylmeadows.eontimer.service.factory

import io.github.dylmeadows.eontimer.model.TimerModel
import io.github.dylmeadows.eontimer.model.timer.Gen5TimerMode
import io.github.dylmeadows.eontimer.model.timer.Gen5TimerModel
import io.github.dylmeadows.eontimer.service.CalibrationService
import io.github.dylmeadows.eontimer.service.factory.timer.DelayTimer
import io.github.dylmeadows.eontimer.service.factory.timer.EnhancedEntralinkTimer
import io.github.dylmeadows.eontimer.service.factory.timer.EntralinkTimer
import io.github.dylmeadows.eontimer.service.factory.timer.SecondTimer
import io.github.dylmeadows.eontimer.util.asFlux
import io.github.dylmeadows.eontimer.util.reactor.TimerState
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.time.Duration
import javax.annotation.PostConstruct

@Component
class Gen5TimerFactory @Autowired constructor(
    private val timerModel: TimerModel,
    private val gen5TimerModel: Gen5TimerModel,
    private val delayTimer: DelayTimer,
    private val secondTimer: SecondTimer,
    private val entralinkTimer: EntralinkTimer,
    private val enhancedEntralinkTimer: EnhancedEntralinkTimer,
    private val calibrationService: CalibrationService) : TimerFactory {

    private val delayCalibration: Long
        get() = delayTimer.calibrate(gen5TimerModel.targetDelay, gen5TimerModel.delayHit)

    private val secondCalibration: Long
        get() = secondTimer.calibrate(gen5TimerModel.targetSecond, gen5TimerModel.secondHit)

    private val entralinkCalibration: Long
        get() = entralinkTimer.calibrate(gen5TimerModel.targetDelay, gen5TimerModel.delayHit - secondCalibration)

    private val advancesCalibration: Long
        get() = enhancedEntralinkTimer.calibrate(gen5TimerModel.targetAdvances, gen5TimerModel.actualAdvances)

    @PostConstruct
    private fun initialize() {
        listOf(
            gen5TimerModel.modeProperty,
            gen5TimerModel.calibrationProperty,
            gen5TimerModel.targetDelayProperty,
            gen5TimerModel.targetSecondProperty,
            gen5TimerModel.entralinkCalibrationProperty,
            gen5TimerModel.frameCalibrationProperty,
            gen5TimerModel.targetAdvancesProperty)
            .map { it.asFlux() }
            .forEach {
                it.subscribe {
                    timerModel.stages = stages
                }
            }
    }

    override val stages: List<Duration>
        get() {
            return when (gen5TimerModel.mode) {
                Gen5TimerMode.STANDARD ->
                    secondTimer.createStages(
                        calibrationService.calibrate(gen5TimerModel.calibration),
                        gen5TimerModel.targetSecond)
                Gen5TimerMode.C_GEAR ->
                    delayTimer.createStages(
                        calibrationService.calibrate(gen5TimerModel.calibration),
                        gen5TimerModel.targetSecond,
                        gen5TimerModel.targetDelay)
                Gen5TimerMode.ENTRALINK ->
                    entralinkTimer.createStages(
                        calibrationService.calibrate(gen5TimerModel.calibration),
                        calibrationService.calibrate(gen5TimerModel.entralinkCalibration),
                        gen5TimerModel.targetSecond,
                        gen5TimerModel.targetDelay)
                Gen5TimerMode.ENHANCED_ENTRALINK ->
                    enhancedEntralinkTimer.createStages(
                        calibrationService.calibrate(gen5TimerModel.calibration),
                        calibrationService.calibrate(gen5TimerModel.entralinkCalibration),
                        gen5TimerModel.frameCalibration,
                        gen5TimerModel.targetSecond,
                        gen5TimerModel.targetDelay,
                        gen5TimerModel.targetAdvances)
            }
        }

    override fun createTimer(): Flux<TimerState> {
        return when (gen5TimerModel.mode) {
            Gen5TimerMode.STANDARD ->
                secondTimer.createTimer(
                    calibrationService.calibrate(gen5TimerModel.calibration),
                    gen5TimerModel.targetSecond)
            Gen5TimerMode.C_GEAR ->
                delayTimer.createTimer(
                    calibrationService.calibrate(gen5TimerModel.calibration),
                    gen5TimerModel.targetSecond,
                    gen5TimerModel.targetDelay)
            Gen5TimerMode.ENTRALINK ->
                entralinkTimer.createTimer(
                    calibrationService.calibrate(gen5TimerModel.calibration),
                    calibrationService.calibrate(gen5TimerModel.entralinkCalibration),
                    gen5TimerModel.targetSecond,
                    gen5TimerModel.targetDelay)
            Gen5TimerMode.ENHANCED_ENTRALINK ->
                enhancedEntralinkTimer.createTimer(
                    calibrationService.calibrate(gen5TimerModel.calibration),
                    calibrationService.calibrate(gen5TimerModel.entralinkCalibration),
                    gen5TimerModel.frameCalibration,
                    gen5TimerModel.targetSecond,
                    gen5TimerModel.targetDelay,
                    gen5TimerModel.targetAdvances)
        }
    }

    override fun calibrate() {
        when (gen5TimerModel.mode) {
            Gen5TimerMode.C_GEAR -> {
                gen5TimerModel.calibration += calibrationService.calibrate(delayCalibration)
            }
            Gen5TimerMode.STANDARD -> {
                gen5TimerModel.calibration += calibrationService.calibrate(secondCalibration)
            }
            Gen5TimerMode.ENTRALINK -> {
                gen5TimerModel.calibration += calibrationService.calibrate(secondCalibration)
                gen5TimerModel.entralinkCalibration += calibrationService.calibrate(entralinkCalibration)
            }
            Gen5TimerMode.ENHANCED_ENTRALINK -> {
                gen5TimerModel.calibration += calibrationService.calibrate(secondCalibration)
                gen5TimerModel.entralinkCalibration += calibrationService.calibrate(entralinkCalibration)
                gen5TimerModel.frameCalibration += advancesCalibration
            }
        }
    }
}