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
    private val calibrationService: CalibrationService,
    private val timerSettings: TimerSettingsModel) {

    fun createStages(preTimer: Long, targetFrame: Long, calibration: Long): List<Duration> {
        return listOf(
            stage1(preTimer),
            stage2(targetFrame, calibration))
            .map(Duration::ofMillis)
    }

    private fun stage1(preTimer: Long): Long =
        preTimer

    private fun stage2(targetFrame: Long, calibration: Long): Long {
        return calibrationService.toMillis(targetFrame) + calibration
    }

    fun start(preTimer: Long, targetFrame: Long, calibration: Long): Flux<TimerState> {
        return FluxFactory.timer(timerSettings.refreshInterval.milliseconds,
            createStages(preTimer, targetFrame, calibration))
    }
}