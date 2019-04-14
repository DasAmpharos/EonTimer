package io.github.dylmeadows.eontimer.service.factory.timer

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class EntralinkTimer @Autowired constructor(
    private val delayTimer: DelayTimer) {

    fun createStages(targetDelay: Long, targetSecond: Long, entralinkCalibration: Long, calibration: Long): List<Duration> {
        val stages = delayTimer.createStages(targetDelay, targetSecond, calibration)
        return listOf(
            stage1(stages),
            stage2(stages, entralinkCalibration))
            .map(Duration::ofMillis)
    }

    fun calibrate(targetDelay: Long, delayHit: Long): Long {
        return delayTimer.calibrate(targetDelay, delayHit)
    }

    private fun stage1(stages: List<Duration>): Long {
        return stages[0].toMillis() + 250
    }

    private fun stage2(stages: List<Duration>, entralinkCalibration: Long): Long {
        return stages[1].toMillis() - entralinkCalibration
    }
}
