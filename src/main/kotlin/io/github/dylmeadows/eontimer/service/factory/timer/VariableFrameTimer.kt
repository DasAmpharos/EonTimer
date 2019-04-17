package io.github.dylmeadows.eontimer.service.factory.timer

import io.github.dylmeadows.eontimer.model.settings.TimerSettingsModel
import io.github.dylmeadows.eontimer.service.CalibrationService
import io.github.dylmeadows.eontimer.service.Timers
import io.github.dylmeadows.eontimer.util.INDEFINITE
import io.github.dylmeadows.eontimer.util.milliseconds
import io.github.dylmeadows.eontimer.util.reactor.TimerState
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.time.Duration

@Service
class VariableFrameTimer @Autowired constructor(
    private val timerSettings: TimerSettingsModel,
    private val calibrationService: CalibrationService) {

    var targetFrame = -1L

    fun createStages(preTimer: Long): List<Duration> {
        return listOf(
            preTimer.milliseconds,
            INDEFINITE)
    }

    fun createTimer(preTimer: Long, calibration: Long): Flux<TimerState> {
        val period = timerSettings.refreshInterval.milliseconds
        return Flux.create { emitter ->
            targetFrame = -1L
            val job = GlobalScope.launch {
                var elapsed = Timers.fixedTimer(period, preTimer.milliseconds) {
                    emitter.next(TimerState(it, preTimer.milliseconds))
                }
                elapsed = Timers.variableTimer(period, elapsed - preTimer.milliseconds,
                    { emitter.next(TimerState(INDEFINITE - it, INDEFINITE)) },
                    { targetFrame != -1L })
                val duration = calibrationService.toMillis(targetFrame).milliseconds
                Timers.fixedTimer(period, duration + calibration.milliseconds, elapsed) {
                    emitter.next(TimerState(it, duration))
                }
                emitter.complete()
            }
            emitter.onDispose(job::cancel)
            emitter.onCancel(job::cancel)
        }
    }
}