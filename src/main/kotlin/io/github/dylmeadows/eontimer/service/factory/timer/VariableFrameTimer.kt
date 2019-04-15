package io.github.dylmeadows.eontimer.service.factory.timer

import io.github.dylmeadows.eontimer.model.settings.TimerSettingsModel
import io.github.dylmeadows.eontimer.service.CalibrationService
import io.github.dylmeadows.eontimer.util.INDEFINITE
import io.github.dylmeadows.eontimer.util.emitTo
import io.github.dylmeadows.eontimer.util.milliseconds
import io.github.dylmeadows.eontimer.util.reactor.FluxFactory
import io.github.dylmeadows.eontimer.util.reactor.TimerState
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.time.Duration
import java.time.Instant

@Service
class VariableFrameTimer @Autowired constructor(
    private val timerSettings: TimerSettingsModel,
    private val calibrationService: CalibrationService) {

    var targetFrame: Long = -1L

    fun start(preTimer: Long, calibration: Long): Flux<TimerState> {
        /* TODO:
            [] design mechanism for setting target frame
            [] implement timer
         */
        return Flux.create { emitter ->
        }
        /*if (!::timerJob.isInitialized || !timerState.running) {
            targetFrame = -1
            timerState.running = true
            timerJob = GlobalScope.launch(Dispatchers.JavaFx) {
                timerState.currentStage = 0L
                timerState.nextStage = TimerConstants.INFINITE_TIME_SPAN
                var lastTimestamp = System.currentTimeMillis()
                while (targetFrame < calibrationService.toDelays(timerState.currentStage)) {
                    delay(timerSettings.refreshInterval.toLong())
                    val now = System.currentTimeMillis()
                    val delta = now - lastTimestamp
                    timerState.currentStage += delta
                    lastTimestamp = now
                }

                val time = calibrationService.toMillis(targetFrame)
                countdownTimer.start(Collections.singletonList(time + (System.currentTimeMillis() - lastTimestamp)))
                stop()
            }
        }*/
    }
}