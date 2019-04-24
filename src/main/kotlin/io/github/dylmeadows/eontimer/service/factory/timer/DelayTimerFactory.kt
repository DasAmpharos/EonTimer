package io.github.dylmeadows.eontimer.service.factory.timer

import io.github.dylmeadows.eontimer.model.timer.TimerConstants
import io.github.dylmeadows.eontimer.service.CalibrationService
import io.github.dylmeadows.eontimer.util.milliseconds
import io.github.dylmeadows.eontimer.util.toMinimumLength
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Duration
import kotlin.math.absoluteValue

@Service
class DelayTimerFactory @Autowired constructor(
    private val secondTimer: SecondTimerFactory,
    private val calibrationService: CalibrationService) {

    fun createStages(targetSecond: Long, targetDelay: Long, calibration: Long): List<Duration> {
        return listOf(stage1(targetSecond, targetDelay, calibration), stage2(targetDelay, calibration))
    }

    private fun stage1(targetSecond: Long, targetDelay: Long, calibration: Long): Duration {
        return (secondTimer.createStages(targetSecond, calibration)[0].toMillis()
            - calibrationService.toMillis(targetDelay))
            .toMinimumLength()
            .milliseconds
    }

    private fun stage2(targetDelay: Long, calibration: Long): Duration {
        return (calibrationService.toMillis(targetDelay) - calibration)
            .milliseconds
    }

    fun calibrate(targetDelay: Long, delayHit: Long): Long {
        val delta = calibrationService.toMillis(delayHit) - calibrationService.toMillis(targetDelay)
        return when {
            delta.absoluteValue <= TimerConstants.CLOSE_THRESHOLD ->
                (TimerConstants.CLOSE_UPDATE_FACTOR * delta).toLong()
            else ->
                (TimerConstants.UPDATE_FACTOR * delta).toLong()
        }
    }
}