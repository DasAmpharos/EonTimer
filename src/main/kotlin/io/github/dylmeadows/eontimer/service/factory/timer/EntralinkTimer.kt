package io.github.dylmeadows.eontimer.service.factory.timer

import io.github.dylmeadows.eontimer.util.milliseconds
import io.github.dylmeadows.eontimer.util.reactor.TimerState
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.time.Duration

@Service
class EntralinkTimer @Autowired constructor(
    private val delayTimer: DelayTimer) {

    fun createStages(targetSecond: Long, targetDelay: Long, entralinkCalibration: Long, calibration: Long): List<Duration> {
        val stages = delayTimer.createStages(targetSecond, targetDelay, calibration)
        return listOf(
            stage1(stages),
            stage2(stages, entralinkCalibration))
    }

    fun createTimer(targetSecond: Long, targetDelay: Long, entralinkCalibration: Long, calibration: Long): Flux<TimerState> {
        // TODO: fix this
        return Flux.empty()
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
