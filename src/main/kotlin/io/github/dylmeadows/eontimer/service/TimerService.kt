package io.github.dylmeadows.eontimer.service

import io.github.dylmeadows.eontimer.model.TimerModel
import io.github.dylmeadows.eontimer.model.TimerState
import io.github.dylmeadows.eontimer.model.settings.TimerSettingsModel
import kotlinx.coroutines.*
import kotlinx.coroutines.javafx.JavaFx
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TimerService @Autowired constructor(
    private val timerModel: TimerModel,
    private val timerState: TimerState,
    private val timerSettingsModel: TimerSettingsModel) {

    private lateinit var timerJob: Job

    fun start() {
        if (!::timerJob.isInitialized || !timerJob.isActive)
            timerJob = GlobalScope.launch(Dispatchers.JavaFx) {
                var overlap = 0L
                timerModel.stages
                    .forEach {
                        timerState.currentStage = it
                        timerState.remaining = it + overlap
                        var lastTimestamp = System.currentTimeMillis()
                        while (timerState.remaining > 0) {
                            val now = System.currentTimeMillis()
                            val delta = now - lastTimestamp
                            timerState.remaining -= delta
                            lastTimestamp = now

                            delay(timerSettingsModel.refreshInterval.toLong())
                        }
                        overlap = timerState.remaining
                    }
            }
    }

    fun stop() {
        if (::timerJob.isInitialized && timerJob.isActive)
            timerJob.cancel()
    }
}