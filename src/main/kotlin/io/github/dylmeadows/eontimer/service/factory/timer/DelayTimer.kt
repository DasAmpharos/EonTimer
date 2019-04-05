package io.github.dylmeadows.eontimer.service.factory.timer

import io.github.dylmeadows.eontimer.model.timer.TimerConstants
import io.github.dylmeadows.eontimer.service.CalibrationService
import io.github.dylmeadows.eontimer.util.normalize
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.math.absoluteValue

@Service
class DelayTimer @Autowired constructor(
    private val secondTimer: SecondTimer,
    private val calibrationService: CalibrationService) {

    fun createStages(calibration: Long, targetSecond: Long, targetDelay: Long): List<Long> {
        return listOf(
            createStage1(calibration, targetSecond, targetDelay),
            createStage2(calibration, targetDelay))
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

    private fun createStage1(calibration: Long, targetSecond: Long, targetDelay: Long): Long {
        return (secondTimer.createStages(calibration, targetSecond)[0]
            - calibrationService.toMillis(targetDelay))
            .normalize()
    }

    private fun createStage2(calibration: Long, targetDelay: Long): Long {
        return calibrationService.toMillis(targetDelay) - calibration
    }
}