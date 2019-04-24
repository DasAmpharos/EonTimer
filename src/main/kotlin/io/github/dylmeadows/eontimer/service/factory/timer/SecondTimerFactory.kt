package io.github.dylmeadows.eontimer.service.factory.timer

import io.github.dylmeadows.eontimer.util.milliseconds
import io.github.dylmeadows.eontimer.util.toMinimumLength
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class SecondTimerFactory {

    fun createStages(targetSecond: Long, calibration: Long): List<Duration> {
        return listOf(stage1(targetSecond, calibration))
    }

    private fun stage1(targetSecond: Long, calibration: Long): Duration {
        return (targetSecond * 1000 + calibration + 200).toMinimumLength()
            .milliseconds
    }

    fun calibrate(targetSecond: Long, secondHit: Long): Long {
        return when {
            secondHit < targetSecond -> (targetSecond - secondHit) * 1000 - 500
            secondHit > targetSecond -> (targetSecond - secondHit) * 1000 + 500
            else -> 0
        }
    }
}