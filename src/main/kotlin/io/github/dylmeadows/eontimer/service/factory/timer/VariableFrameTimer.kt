package io.github.dylmeadows.eontimer.service.factory.timer

import io.github.dylmeadows.eontimer.model.TimerState
import io.github.dylmeadows.eontimer.model.settings.TimerSettingsModel
import io.github.dylmeadows.eontimer.model.timer.TimerConstants
import io.github.dylmeadows.eontimer.service.CalibrationService
import io.github.dylmeadows.eontimer.service.CountdownTimer
import kotlinx.coroutines.*
import kotlinx.coroutines.javafx.JavaFx
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class VariableFrameTimer @Autowired constructor(
    private val timerState: TimerState,
    private val timerSettings: TimerSettingsModel,
    private val calibrationService: CalibrationService,
    private val countdownTimer: CountdownTimer) {

    var targetFrame: Long = 0
    private lateinit var timerJob: Job

    fun start() {
        if (!::timerJob.isInitialized || !timerState.running) {
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
        }
    }

    fun stop() {
        if (::timerJob.isInitialized && timerState.running) {
            timerState.running = false
            if (timerJob.isActive)
                timerJob.cancel()

            timerState.remaining = 0
            timerState.currentStage = 0
            timerState.nextStage = TimerConstants.INFINITE_TIME_SPAN
        }
    }
}