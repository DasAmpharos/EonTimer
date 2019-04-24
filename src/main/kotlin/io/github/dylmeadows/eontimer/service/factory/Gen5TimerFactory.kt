package io.github.dylmeadows.eontimer.service.factory

import io.github.dylmeadows.eontimer.model.TimerState
import io.github.dylmeadows.eontimer.model.timer.Gen5TimerMode
import io.github.dylmeadows.eontimer.model.timer.Gen5TimerModel
import io.github.dylmeadows.eontimer.service.CalibrationService
import io.github.dylmeadows.eontimer.service.factory.timer.DelayTimerFactory
import io.github.dylmeadows.eontimer.service.factory.timer.EnhancedEntralinkTimerFactory
import io.github.dylmeadows.eontimer.service.factory.timer.EntralinkTimerFactory
import io.github.dylmeadows.eontimer.service.factory.timer.SecondTimerFactory
import io.github.dylmeadows.eontimer.util.asFlux
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.Duration
import javax.annotation.PostConstruct

@Component
class Gen5TimerFactory @Autowired constructor(
    private val timerState: TimerState,
    private val gen5TimerModel: Gen5TimerModel,
    private val delayTimerFactory: DelayTimerFactory,
    private val secondTimerFactory: SecondTimerFactory,
    private val entralinkTimerFactory: EntralinkTimerFactory,
    private val enhancedEntralinkTimerFactory: EnhancedEntralinkTimerFactory,
    private val calibrationService: CalibrationService) : TimerFactory {

    private val delayCalibration: Long
        get() = delayTimerFactory.calibrate(gen5TimerModel.targetDelay, gen5TimerModel.delayHit)

    private val secondCalibration: Long
        get() = secondTimerFactory.calibrate(gen5TimerModel.targetSecond, gen5TimerModel.secondHit)

    private val entralinkCalibration: Long
        get() = entralinkTimerFactory.calibrate(gen5TimerModel.targetDelay, gen5TimerModel.delayHit - secondCalibration)

    private val advancesCalibration: Long
        get() = enhancedEntralinkTimerFactory.calibrate(gen5TimerModel.targetAdvances, gen5TimerModel.actualAdvances)

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
                    timerState.update(stages)
                }
            }
    }

    override val stages: List<Duration>
        get() {
            return when (gen5TimerModel.mode) {
                Gen5TimerMode.STANDARD ->
                    secondTimerFactory.createStages(
                        gen5TimerModel.targetSecond,
                        calibrationService.calibrate(gen5TimerModel.calibration))
                Gen5TimerMode.C_GEAR ->
                    delayTimerFactory.createStages(
                        gen5TimerModel.targetSecond,
                        gen5TimerModel.targetDelay,
                        calibrationService.calibrate(gen5TimerModel.calibration))
                Gen5TimerMode.ENTRALINK ->
                    entralinkTimerFactory.createStages(
                        gen5TimerModel.targetSecond,
                        gen5TimerModel.targetDelay,
                        calibrationService.calibrate(gen5TimerModel.calibration),
                        calibrationService.calibrate(gen5TimerModel.entralinkCalibration))
                Gen5TimerMode.ENHANCED_ENTRALINK ->
                    enhancedEntralinkTimerFactory.createStages(
                        gen5TimerModel.targetSecond,
                        gen5TimerModel.targetDelay,
                        gen5TimerModel.targetAdvances,
                        calibrationService.calibrate(gen5TimerModel.calibration),
                        calibrationService.calibrate(gen5TimerModel.entralinkCalibration),
                        gen5TimerModel.frameCalibration)
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