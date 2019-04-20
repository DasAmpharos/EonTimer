package io.github.dylmeadows.eontimer.service.factory.timer

import io.github.dylmeadows.eontimer.model.settings.TimerSettingsModel
import io.github.dylmeadows.eontimer.util.milliseconds
import io.github.dylmeadows.eontimer.util.toMinimumLength
import io.github.dylmeadows.eontimer.util.reactor.FluxFactory
import io.github.dylmeadows.eontimer.util.reactor.TimerState
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.time.Duration

@Service
class SecondTimer @Autowired constructor(
    private val timerSettings: TimerSettingsModel) {

    fun createStages(targetSecond: Long, calibration: Long): List<Duration> {
        return listOf(
            stage1(calibration, targetSecond))
    }

    fun createTimer(targetSecond: Long, calibration: Long): Flux<TimerState> {
        return FluxFactory.fixedTimer(timerSettings.refreshInterval.milliseconds,
            createStages(targetSecond, calibration))
    }

    fun calibrate(targetSecond: Long, secondHit: Long): Long {
        return when {
            secondHit < targetSecond -> (targetSecond - secondHit) * 1000 - 500
            secondHit > targetSecond -> (targetSecond - secondHit) * 1000 + 500
            else -> 0
        }
    }

    private fun stage1(calibration: Long, targetSecond: Long): Duration {
        return (targetSecond * 1000 + calibration + 200).toMinimumLength()
            .milliseconds
    }
}