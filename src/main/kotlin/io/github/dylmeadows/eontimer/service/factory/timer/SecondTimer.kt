package io.github.dylmeadows.eontimer.service.factory.timer

import io.github.dylmeadows.eontimer.util.normalize
import org.springframework.stereotype.Service

@Service
class SecondTimer {

    fun createStages(calibration: Long, targetSecond: Long): List<Long> {
        return listOf(
            createStage1(calibration, targetSecond))
    }

    fun calibrate(targetSecond: Long, secondHit: Long): Long {
        return when {
            secondHit < targetSecond -> (targetSecond - secondHit) * 1000 - 500
            secondHit > targetSecond -> (targetSecond - secondHit) * 1000 + 500
            else -> 0
        }
    }

    private fun createStage1(calibration: Long, targetSecond: Long): Long {
        return (targetSecond * 1000 + calibration + 200).normalize()
    }
}