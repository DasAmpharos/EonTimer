package io.github.dylmeadows.eontimer.service.factory.timer

import io.github.dylmeadows.eontimer.model.settings.TimerSettingsModel
import io.github.dylmeadows.eontimer.service.CalibrationService
import io.github.dylmeadows.eontimer.util.milliseconds
import io.github.dylmeadows.eontimer.util.reactor.FluxFactory
import io.github.dylmeadows.eontimer.util.reactor.TimerState
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.time.Duration

@Service
class FixedFrameTimer @Autowired constructor(
    private val timerSettings: TimerSettingsModel,
    private val calibrationService: CalibrationService) {

    fun createStages(preTimer: Long, targetFrame: Long, calibration: Long): List<Duration> {
        return listOf(
            stage1(preTimer),
            stage2(targetFrame, calibration))
    }

    fun createTimer(preTimer: Long, targetFrame: Long, calibration: Long): Flux<TimerState> {
        return FluxFactory.fixedTimer(timerSettings.refreshInterval.milliseconds,
            createStages(preTimer, targetFrame, calibration))
    }

    private fun stage1(preTimer: Long): Duration =
        preTimer.milliseconds

    private fun stage2(targetFrame: Long, calibration: Long): Duration {
        return (calibrationService.toMillis(targetFrame) + calibration)
            .milliseconds
    }
}