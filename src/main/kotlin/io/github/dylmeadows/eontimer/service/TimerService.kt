package io.github.dylmeadows.eontimer.service

import io.github.dylmeadows.eontimer.model.TimerModel
import io.github.dylmeadows.eontimer.model.TimerState
import io.github.dylmeadows.eontimer.model.settings.TimerSettingsModel
import io.github.dylmeadows.eontimer.model.timer.TimerConstants
import io.github.dylmeadows.eontimer.util.JavaFxScheduler
import io.github.dylmeadows.eontimer.util.asFlux
import kotlinx.coroutines.*
import kotlinx.coroutines.javafx.JavaFx
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import javax.annotation.PostConstruct

@Service
class TimerService @Autowired constructor(
    private val timerModel: TimerModel,
    private val timerState: TimerState,
    private val timerSettingsModel: TimerSettingsModel) {

    private lateinit var timerJob: Job

    @PostConstruct
    private fun initialize() {
        timerModel.stagesProperty.asFlux()
            .subscribeOn(JavaFxScheduler.platform())
            .subscribe { resetState() }
    }

    fun start() {
        if (!::timerJob.isInitialized || !timerState.running) {
            timerState.running = true
            timerJob = GlobalScope.launch(Dispatchers.JavaFx) {
                var overlap = 0L
                var totalTime = timerModel.stages.sum()
                timerState.minutesBeforeTarget = totalTime / 60000

                for (index in timerModel.stages.indices) {
                    timerState.currentStage = getStage(timerModel.stages, index)
                    timerState.nextStage = getStage(timerModel.stages, index + 1)
                    timerState.remaining = timerState.currentStage + overlap

                    var lastTimestamp = System.currentTimeMillis()
                    while (timerState.remaining > 0) {
                        delay(timerSettingsModel.refreshInterval.toLong())
                        val now = System.currentTimeMillis()
                        val delta = now - lastTimestamp
                        lastTimestamp = now

                        totalTime -= delta
                        timerState.minutesBeforeTarget = totalTime / 60000
                        timerState.remaining -= delta
                    }
                    overlap = timerState.remaining
                }
                stop()
            }
        }
    }

    fun stop() {
        if (::timerJob.isInitialized && timerState.running) {
            timerState.running = false
            if (timerJob.isActive)
                timerJob.cancel()
            resetState()
        }
    }

    private fun getStage(stages: List<Long>, index: Int): Long {
        return Optional.ofNullable(stages)
            .filter { it.isNotEmpty() }
            .filter { it.size > index }
            .map { it[index] }
            .orElse(TimerConstants.NULL_TIME_SPAN)
    }

    private fun resetState() {
        timerState.minutesBeforeTarget = timerModel.stages.sum() / 60000
        timerState.currentStage = getStage(timerModel.stages, 0)
        timerState.nextStage = getStage(timerModel.stages, 1)
    }
}