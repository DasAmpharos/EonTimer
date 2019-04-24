package io.github.dylmeadows.eontimer.service

import io.github.dylmeadows.eontimer.model.TimerState
import io.github.dylmeadows.eontimer.model.settings.TimerSettingsModel
import io.github.dylmeadows.eontimer.util.getStage
import io.github.dylmeadows.eontimer.util.isIndefinite
import io.github.dylmeadows.eontimer.util.milliseconds
import io.github.dylmeadows.eontimer.util.sum
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant
import java.util.*

@Service
class TimerRunnerService @Autowired constructor(
    private val timerState: TimerState,
    private val timerSettings: TimerSettingsModel) {

    private lateinit var timerJob: Job
    var stages: MutableList<Duration> = Collections.emptyList()
        private set
    private var mStages: List<Duration> = Collections.emptyList()
    val isRunning: Boolean get() = timerState.running

    private val period: Duration get() = timerSettings.refreshInterval.milliseconds

    fun start(stages: List<Duration> = mStages) {
        if (!timerState.running) {
            this.mStages = stages
            this.stages = mStages.toMutableList()
            timerJob = GlobalScope.launch(Dispatchers.JavaFx) {
                var stageIndex = 0
                var preElapsed = Duration.ZERO
                timerState.totalTime = stages.sum()
                while (isActive && stageIndex < stages.size) {
                    timerState.currentStage = stages.getStage(stageIndex)
                    timerState.nextStage = stages.getStage(stageIndex + 1)
                    preElapsed = runStage(this, stageIndex, preElapsed) - stages.getStage(stageIndex)
                    stageIndex++
                }
                timerState.running = false
                resetState()
            }
            timerState.running = true
        }
    }

    private suspend fun runStage(scope: CoroutineScope, stageIndex: Int, preElapsed: Duration): Duration {
        var elapsed = preElapsed
        var adjustedDelay = period
        var lastTimestamp = Instant.now()
        while (scope.isActive && elapsed < stages.getStage(stageIndex)) {
            delay(adjustedDelay.toMillis())

            val now = Instant.now()
            val delta = Duration.between(lastTimestamp, now)
            adjustedDelay -= delta - period
            lastTimestamp = now
            elapsed += delta

            timerState.totalElapsed += delta
            if (!stages.getStage(stageIndex).isIndefinite) {
                val remaining = stages.getStage(stageIndex) - elapsed
                timerState.currentRemaining = remaining
            } else {
                timerState.currentRemaining = elapsed
            }
        }
        return elapsed
    }

    fun stop() {
        if (timerState.running) {
            timerJob.cancel()
            timerState.running = false
            resetState()
        }
    }

    private fun resetState() {
        stages = mStages.toMutableList()
        timerState.totalTime = mStages.sum()
        timerState.totalElapsed = Duration.ZERO

        val currentStage = mStages.getStage(0)
        timerState.currentRemaining = if (!currentStage.isIndefinite) currentStage else Duration.ZERO
        timerState.currentStage = mStages.getStage(0)
        timerState.nextStage = mStages.getStage(1)
    }
}