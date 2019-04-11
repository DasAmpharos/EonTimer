package io.github.dylmeadows.eontimer.service

import io.github.dylmeadows.eontimer.model.TimerState
import io.github.dylmeadows.eontimer.model.settings.TimerSettingsModel
import io.github.dylmeadows.eontimer.model.timer.TimerConstants
import kotlinx.coroutines.*
import kotlinx.coroutines.javafx.JavaFx
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.time.Duration
import java.util.*

@Service
class CountdownTimer @Autowired constructor(
    private val timerState: TimerState,
    private val timerSettings: TimerSettingsModel) {

    private lateinit var timerJob: Job
    private var stages: List<Long> = Collections.emptyList()

    private val now: Long get() = System.currentTimeMillis()

    fun fixedInterval(period: Duration, totalDuration: Duration): Flux<Long> {
        return Flux.create<Long> { emitter ->
            val job = GlobalScope.launch {
                var totalTime = 0L
                var lastTimestamp = now
                while (totalTime < totalDuration.toMillis()) {
                    delay(period.toMillis())
                    val delta = now - lastTimestamp
                    emitter.next(delta)
                    totalTime += delta
                    lastTimestamp = now
                }
                emitter.complete()
            }
            emitter.onDispose(job::cancel)
            emitter.onCancel(job::cancel)
        }
    }

    fun start(stages: List<Long>) {
        if (!::timerJob.isInitialized || !timerState.running && stages.isNotEmpty()) {
            this.stages = stages
            timerState.running = true
            timerJob = GlobalScope.launch(Dispatchers.JavaFx) {
                var overlap = 0L
                var totalTime = stages.sum()
                timerState.minutesBeforeTarget = totalTime / 60000

                for (index in stages.indices) {
                    timerState.currentStage = getStage(stages, index)
                    timerState.nextStage = getStage(stages, index + 1)
                    timerState.remaining = timerState.currentStage + overlap

                    var lastTimestamp = System.currentTimeMillis()
                    while (timerState.remaining > 0) {
                        val now = System.currentTimeMillis()
                        val delta = now - lastTimestamp
                        lastTimestamp = now

                        totalTime -= delta
                        timerState.minutesBeforeTarget = totalTime / 60000
                        timerState.remaining -= delta

                        delay(timerSettings.refreshInterval.toLong())
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
        timerState.minutesBeforeTarget = stages.sum() / 60000
        timerState.currentStage = getStage(stages, 0)
        timerState.nextStage = getStage(stages, 1)
        timerState.remaining = timerState.currentStage
    }
}