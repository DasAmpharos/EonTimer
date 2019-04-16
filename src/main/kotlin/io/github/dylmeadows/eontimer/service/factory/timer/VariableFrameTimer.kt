package io.github.dylmeadows.eontimer.service.factory.timer

import io.github.dylmeadows.eontimer.model.settings.TimerSettingsModel
import io.github.dylmeadows.eontimer.service.CalibrationService
import io.github.dylmeadows.eontimer.util.getValue
import io.github.dylmeadows.eontimer.util.reactor.TimerState
import io.github.dylmeadows.eontimer.util.setValue
import javafx.beans.property.LongProperty
import javafx.beans.property.SimpleLongProperty
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class VariableFrameTimer @Autowired constructor(
    private val timerSettings: TimerSettingsModel,
    private val calibrationService: CalibrationService) {

    private val targetFrameProperty: LongProperty = SimpleLongProperty(-1)
    var targetFrame by targetFrameProperty

    fun start(preTimer: Long, calibration: Long): Flux<TimerState> {
        /* TODO:
            [] design mechanism for setting target frame
            [] implement timer
         */
        return Flux.create { emitter ->
            //            val period = timerSettings.refreshInterval.milliseconds
//            val durations = listOf(preTimer.milliseconds, INDEFINITE)
//            GlobalScope.launch {
//                var elapsed = preTimer.milliseconds.asTimer(period, emitter)
//
//            }
//            FluxFactory.asTimer(period, durations)
//
//            val job = GlobalScope.launch {
//                var elapsed = Duration.ZERO
//                durations.forEach { duration ->
//                    var delay = period
//                    var lastTimestamp = Instant.now()
//                    while (elapsed < duration) {
//                        delay(delay.toMillis())
//
//                        val now = Instant.now()
//                        val delta = Duration.between(lastTimestamp, now)
//                        // adjust delay
//                        if ((duration - elapsed) < period) {
//                            delay = duration - elapsed
//                        } else {
//                            delay -= delta - period
//                        }
//                        lastTimestamp = now
//                        elapsed += delta
//
//                        emitter.next(TimerState(delta, elapsed, duration))
//                    }
//                    elapsed -= duration
//                }
//                emitter.complete()
//            }
//            emitter.onDispose(job::cancel)
//            emitter.onCancel(job::cancel)
//        }
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
}