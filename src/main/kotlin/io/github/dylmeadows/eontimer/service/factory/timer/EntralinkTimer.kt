package io.github.dylmeadows.eontimer.service.factory.timer

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class EntralinkTimer @Autowired constructor(
    private val delayTimer: DelayTimer) {

    fun createStages(calibration: Long, entralinkCalibration: Long, targetSecond: Long, targetDelay: Long): List<Long> {
        val stages = delayTimer.createStages(calibration, targetSecond, targetDelay)
        return listOf(
            createStage1(stages),
            createStage2(stages, entralinkCalibration))
    }

    fun calibrate(targetDelay: Long, delayHit: Long): Long {
        return delayTimer.calibrate(targetDelay, delayHit)
    }

    private fun createStage1(stages: List<Long>): Long {
        return stages[0] + 250
    }

    private fun createStage2(stages: List<Long>, entralinkCalibration: Long): Long {
        return stages[1] - entralinkCalibration
    }
}
