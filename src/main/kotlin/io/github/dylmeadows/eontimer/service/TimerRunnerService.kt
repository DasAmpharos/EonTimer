package io.github.dylmeadows.eontimer.service

import io.github.dylmeadows.eontimer.model.TimerState
import io.github.dylmeadows.eontimer.model.settings.TimerSettingsModel
import io.github.dylmeadows.eontimer.service.action.TimerActionService
import io.github.dylmeadows.eontimer.util.Stack
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
    private val timerSettings: TimerSettingsModel,
    private val timerActionService: TimerActionService) {

    private lateinit var timerJob: Job
    var stages: MutableList<Duration> = Collections.emptyList()
        private set
    private var mStages: List<Duration> = Collections.emptyList()

    private var totalTime: Duration
        get() = timerState.totalTime
        set(value) {
            timerState.totalTime = value
        }
    private var totalElapsed: Duration
        get() = timerState.totalElapsed
        set(value) {
            timerState.totalElapsed = value
        }
    private var currentStage: Duration
        get() = timerState.currentStage
        set(value) {
            timerState.currentStage = value
        }
    private var currentRemaining: Duration
        get() = timerState.currentRemaining
        set(value) {
            timerState.currentRemaining = value
        }
    private var nextStage: Duration
        get() = timerState.nextStage
        set(value) {
            timerState.nextStage = value
        }
    private var isRunning: Boolean
        get() = timerState.running
        private set(value) {
            timerState.running = value
        }
    private val actionInterval: Stack<Duration>
        get() = Stack(timerActionService.actionInterval
            .filter { it < currentStage })

    private val period: Duration get() = timerSettings.refreshInterval.milliseconds

    fun start(stages: List<Duration> = mStages) {
        if (!isRunning) {
            this.mStages = stages
            this.stages = stages.toMutableList()

            updateState()
            totalTime = stages.sum()
            nextStage = stages.getStage(1)
            timerJob = GlobalScope.launch(Dispatchers.JavaFx) {
                var stageIndex = 0
                var preElapsed = Duration.ZERO
                while (isActive && stageIndex < stages.size) {
                    preElapsed = runStage(this, stageIndex, preElapsed) - stages.getStage(stageIndex)
                    stageIndex++
                }
                isRunning = false
                resetState()
            }
            isRunning = true
        }
    }

    private suspend fun runStage(scope: CoroutineScope, stageIndex: Int, preElapsed: Duration): Duration {
        var elapsed = preElapsed
        var adjustedDelay = period
        var lastTimestamp = Instant.now()
        currentStage = stages.getStage(stageIndex)

        val actionInterval = this.actionInterval
        while (scope.isActive && elapsed < currentStage) {
            delay(adjustedDelay.toMillis())

            val now = Instant.now()
            val delta = Duration.between(lastTimestamp, now)
            adjustedDelay -= delta - period
            lastTimestamp = now
            elapsed += delta

            updateState(stageIndex, delta, elapsed)
            if (!currentStage.isIndefinite && currentRemaining <= actionInterval.peek()) {
                timerActionService.invokeAction()
                actionInterval.pop()
            }
        }
        return elapsed
    }

    fun stop() {
        if (timerState.running) {
            timerJob.cancel()
            isRunning = false
            resetState()
        }
    }

    private fun updateState(stageIndex: Int = 0,
                            delta: Duration = Duration.ZERO,
                            elapsed: Duration = Duration.ZERO) {
        currentStage = stages.getStage(stageIndex)
        currentRemaining = if (!currentStage.isIndefinite)
            currentStage - elapsed
        else
            elapsed
        totalElapsed += delta
    }

    private fun resetState() {
        stages = mStages.toMutableList()

        totalTime = mStages.sum()
        totalElapsed = Duration.ZERO
        currentStage = mStages.getStage(0)
        currentRemaining = if (!currentStage.isIndefinite) currentStage else Duration.ZERO
        nextStage = mStages.getStage(1)
    }
}