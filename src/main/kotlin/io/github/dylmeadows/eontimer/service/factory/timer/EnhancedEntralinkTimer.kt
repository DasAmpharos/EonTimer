package io.github.dylmeadows.eontimer.service.factory.timer

import io.github.dylmeadows.eontimer.model.settings.TimerSettingsModel
import io.github.dylmeadows.eontimer.model.timer.Gen5TimerConstants
import io.github.dylmeadows.eontimer.util.milliseconds
import io.github.dylmeadows.eontimer.util.reactor.FluxFactory
import io.github.dylmeadows.eontimer.util.reactor.TimerState
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.time.Duration

@Service
class EnhancedEntralinkTimer @Autowired constructor(
    private val entralinkTimer: EntralinkTimer,
    private val timerSettings: TimerSettingsModel) {

    fun createStages(targetSecond: Long, targetDelay: Long, targetAdvances: Long,
                     calibration: Long, entralinkCalibration: Long, frameCalibration: Long): List<Duration> {
        val stages = entralinkTimer.createStages(targetDelay, targetSecond, calibration, entralinkCalibration)
        return listOf(
            stage1(stages),
            stage2(stages),
            stage3(targetAdvances, frameCalibration))
            .map(Duration::ofMillis)
    }

    fun createTimer(targetSecond: Long, targetDelay: Long, targetAdvances: Long,
                    calibration: Long, entralinkCalibration: Long, frameCalibration: Long): Flux<TimerState> {
        return FluxFactory.fixedTimer(timerSettings.refreshInterval.milliseconds,
            createStages(targetSecond, targetDelay, targetAdvances,
                calibration, entralinkCalibration, frameCalibration))
    }

    fun calibrate(targetAdvances: Long, actualAdvances: Long): Long {
        return Math.round((targetAdvances - actualAdvances) / Gen5TimerConstants.ENTRALINK_FRAME_RATE) * 1000

        // TODO: 4/1/19 - determine if this is still needed
        // val npcRate = 1.0 / calibrationService.toMillis(32)
        // return Math.round((targetAdvances - actualAdvances) / (Gen5TimerConstants.ENTRALINK_FRAME_RATE + (npcCount * npcRate))) * 1000
    }

    private fun stage1(stages: List<Duration>): Long = stages[0].toMillis()

    private fun stage2(stages: List<Duration>): Long = stages[1].toMillis()

    private fun stage3(targetAdvances: Long, frameCalibration: Long): Long {
        return Math.round(targetAdvances / Gen5TimerConstants.ENTRALINK_FRAME_RATE) * 1000 + frameCalibration
    }
}