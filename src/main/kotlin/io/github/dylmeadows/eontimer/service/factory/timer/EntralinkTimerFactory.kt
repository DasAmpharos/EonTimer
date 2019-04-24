package io.github.dylmeadows.eontimer.service.factory.timer

import io.github.dylmeadows.eontimer.util.milliseconds
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class EntralinkTimerFactory @Autowired constructor(
    private val delayTimer: DelayTimerFactory) {

    fun createStages(targetSecond: Long, targetDelay: Long, calibration: Long, entralinkCalibration: Long): List<Duration> {
        val stages = delayTimer.createStages(targetSecond, targetDelay, calibration)
        return listOf(stage1(stages), stage2(stages, entralinkCalibration))
    }

    fun calibrate(targetDelay: Long, delayHit: Long): Long {
        return delayTimer.calibrate(targetDelay, delayHit)
    }

    private fun stage1(stages: List<Duration>): Duration {
        return stages[0] + 250L.milliseconds
    }

    private fun stage2(stages: List<Duration>, entralinkCalibration: Long): Duration {
        return stages[1] - entralinkCalibration.milliseconds
    }
}
