package io.github.dylmeadows.eontimer.service.factory

import io.github.dylmeadows.eontimer.model.TimerModel
import io.github.dylmeadows.eontimer.model.settings.TimerSettingsModel
import io.github.dylmeadows.eontimer.model.timer.Gen5TimerMode
import io.github.dylmeadows.eontimer.model.timer.Gen5TimerModel
import io.github.dylmeadows.eontimer.service.CalibrationService
import io.github.dylmeadows.eontimer.service.factory.timer.DelayTimer
import io.github.dylmeadows.eontimer.service.factory.timer.EnhancedEntralinkTimer
import io.github.dylmeadows.eontimer.service.factory.timer.EntralinkTimer
import io.github.dylmeadows.eontimer.service.factory.timer.SecondTimer
import io.github.dylmeadows.eontimer.util.asFlux
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*
import javax.annotation.PostConstruct

@Component
class Gen5TimerFactory @Autowired constructor(
    private val timerModel: TimerModel,
    private val gen5TimerModel: Gen5TimerModel,
    private val delayTimer: DelayTimer,
    private val secondTimer: SecondTimer,
    private val entralinkTimer: EntralinkTimer,
    private val enhancedEntralinkTimer: EnhancedEntralinkTimer,
    timerSettings: TimerSettingsModel,
    calibrationService: CalibrationService) : AbstractTimerFactory(timerSettings, calibrationService) {

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
                    timerModel.stages = createTimer()
                }
            }
    }

    override fun createTimer(): List<Long> {
        return when (gen5TimerModel.mode) {
            Gen5TimerMode.STANDARD ->
                secondTimer.createStages(
                    gen5TimerModel.calibration.calibrate(),
                    gen5TimerModel.targetSecond)
            Gen5TimerMode.C_GEAR ->
                // TODO: fix this
                // delayTimer.createStages(
                //    gen5TimerModel.calibration.calibrate(),
                //    gen5TimerModel.targetSecond,
                //    gen5TimerModel.targetDelay)
                Collections.emptyList()
            Gen5TimerMode.ENTRALINK ->
                entralinkTimer.createStages(
                    gen5TimerModel.calibration.calibrate(),
                    gen5TimerModel.entralinkCalibration.calibrate(),
                    gen5TimerModel.targetSecond,
                    gen5TimerModel.targetDelay)
            Gen5TimerMode.ENHANCED_ENTRALINK ->
                enhancedEntralinkTimer.createStages(
                    gen5TimerModel.calibration.calibrate(),
                    gen5TimerModel.entralinkCalibration.calibrate(),
                    gen5TimerModel.frameCalibration,
                    gen5TimerModel.targetSecond,
                    gen5TimerModel.targetDelay,
                    gen5TimerModel.targetAdvances)
        }
    }

    override fun calibrate() {
        when (gen5TimerModel.mode) {
            Gen5TimerMode.C_GEAR -> {
                gen5TimerModel.calibration += delayCalibration.calibrate()
            }
            Gen5TimerMode.STANDARD -> {
                gen5TimerModel.calibration += secondCalibration.calibrate()
            }
            Gen5TimerMode.ENTRALINK -> {
                gen5TimerModel.calibration += secondCalibration.calibrate()
                gen5TimerModel.entralinkCalibration += entralinkCalibration.calibrate()
            }
            Gen5TimerMode.ENHANCED_ENTRALINK -> {
                gen5TimerModel.calibration += secondCalibration.calibrate()
                gen5TimerModel.entralinkCalibration += entralinkCalibration.calibrate()
                gen5TimerModel.frameCalibration += advancesCalibration
            }
        }
    }
}