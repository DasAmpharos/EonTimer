package io.github.dylmeadows.eontimer.service.factory.timer

import io.github.dylmeadows.eontimer.model.timer.Gen5TimerConstants
import io.github.dylmeadows.eontimer.util.milliseconds
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class EnhancedEntralinkTimerFactory @Autowired constructor(
    private val entralinkTimer: EntralinkTimerFactory) {

    fun createStages(targetSecond: Long, targetDelay: Long, targetAdvances: Long,
                     calibration: Long, entralinkCalibration: Long, frameCalibration: Long): List<Duration> {
        val stages = entralinkTimer.createStages(targetSecond, targetDelay, calibration, entralinkCalibration)
        return listOf(stages[0], stages[1], stage3(targetAdvances, frameCalibration))
    }

    private fun stage3(targetAdvances: Long, frameCalibration: Long): Duration {
        return (Math.round(targetAdvances / Gen5TimerConstants.ENTRALINK_FRAME_RATE) * 1000 + frameCalibration)
            .milliseconds
    }

    fun calibrate(targetAdvances: Long, actualAdvances: Long): Long {
        return Math.round((targetAdvances - actualAdvances) / Gen5TimerConstants.ENTRALINK_FRAME_RATE) * 1000

        // TODO: 4/1/19 - determine if this is still needed
        // val npcRate = 1.0 / calibrationService.toMillis(32)
        // return Math.round((targetAdvances - actualAdvances) / (Gen5TimerConstants.ENTRALINK_FRAME_RATE + (npcCount * npcRate))) * 1000
    }
}