package io.github.dylmeadows.eontimer.service.factory.timer

import io.github.dylmeadows.eontimer.model.settings.TimerSettingsModel
import io.github.dylmeadows.eontimer.service.CalibrationService
import io.github.dylmeadows.eontimer.util.INDEFINITE
import io.github.dylmeadows.eontimer.util.milliseconds
import io.github.dylmeadows.eontimer.util.reactor.FluxFactory
import io.github.dylmeadows.eontimer.util.reactor.TimerState
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

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
            val timer = FluxFactory.timer(timerSettings.refreshInterval.milliseconds,
                listOf(preTimer.milliseconds, INDEFINITE))
                .doOnSubscribe { targetFrame = -1L }
                .doOnNext { emitter.next(it) }
                .doOnError(emitter::error)
                .takeUntil {
                    it.duration == INDEFINITE
                        && targetFrame != -1L
                }
                .share()
            timer.last()
                .flatMapMany { lastState ->
                    val duration = calibrationService.toMillis(targetFrame).milliseconds
                    FluxFactory.timer(timerSettings.refreshInterval.milliseconds,
                        duration - lastState.elapsed)
                }
                .doOnComplete(emitter::complete)
                .doOnNext { emitter.next(it) }
                .doOnError(emitter::error)

            val timerSub = timer.subscribe()
            val targetSub =
            // val countDownTargetTimer

            val sub = FluxFactory.timer(timerSettings.refreshInterval.milliseconds,
                listOf(preTimer.milliseconds, INDEFINITE))
                .doOnSubscribe { targetFrame = -1L }
                .doOnNext { emitter.next(it) }
                .doOnError(emitter::error)
                .takeUntil {
                    it.duration == INDEFINITE
                        && targetFrame != -1L
                }.last()
                .flatMapMany { lastState ->
                    val duration = calibrationService.toMillis(targetFrame).milliseconds
                    FluxFactory.timer(timerSettings.refreshInterval.milliseconds,
                        duration - lastState.elapsed)
                }
                .doOnComplete(emitter::complete)
                .doOnNext { emitter.next(it) }
                .doOnError(emitter::error)
                .subscribe()
            emitter.onDispose(sub::dispose)
            emitter.onCancel(sub::dispose)
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