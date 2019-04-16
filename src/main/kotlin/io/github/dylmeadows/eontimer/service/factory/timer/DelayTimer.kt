package io.github.dylmeadows.eontimer.service.factory.timer

import io.github.dylmeadows.eontimer.model.settings.TimerSettingsModel
import io.github.dylmeadows.eontimer.model.timer.TimerConstants
import io.github.dylmeadows.eontimer.service.CalibrationService
import io.github.dylmeadows.eontimer.util.milliseconds
import io.github.dylmeadows.eontimer.util.normalize
import io.github.dylmeadows.eontimer.util.reactor.FluxFactory
import io.github.dylmeadows.eontimer.util.reactor.TimerState
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.time.Duration
import kotlin.math.absoluteValue

@Service
class DelayTimer @Autowired constructor(
    private val secondTimer: SecondTimer,
    private val calibrationService: CalibrationService,
    private val timerSettings: TimerSettingsModel) {

    fun createStages(targetSecond: Long, targetDelay: Long, calibration: Long): List<Duration> {
        return listOf(
            stage1(targetSecond, targetDelay, calibration),
            stage2(targetDelay, calibration))
            .map { stage -> Duration.ofMillis(stage) }
    }

    fun calibrate(targetDelay: Long, delayHit: Long): Long {
        val offset = calibrationService.toMillis(delayHit) - calibrationService.toMillis(targetDelay)
        return when {
            offset.absoluteValue <= TimerConstants.CLOSE_THRESHOLD ->
                (TimerConstants.CLOSE_UPDATE_FACTOR * offset).toLong()
            else ->
                (TimerConstants.UPDATE_FACTOR * offset).toLong()
        }
    }

    private fun stage1(targetSecond: Long, targetDelay: Long, calibration: Long): Long {
        return (secondTimer.createStages(calibration, targetSecond)[0].toMillis()
            - calibrationService.toMillis(targetDelay))
            .normalize()
    }

    private fun stage2(targetDelay: Long, calibration: Long): Long {
        return calibrationService.toMillis(targetDelay) - calibration
    }

    fun start(targetSecond: Long, targetDelay: Long, calibration: Long): Flux<TimerState> {
        return FluxFactory.timer(timerSettings.refreshInterval.milliseconds,
            createStages(targetSecond, targetDelay, calibration))
    }
}