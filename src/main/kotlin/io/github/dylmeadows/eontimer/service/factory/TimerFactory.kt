package io.github.dylmeadows.eontimer.service.factory

import io.github.dylmeadows.eontimer.model.TimerState
import io.github.dylmeadows.eontimer.util.getStage
import io.github.dylmeadows.eontimer.util.isIndefinite
import io.github.dylmeadows.eontimer.util.sum
import java.time.Duration

interface TimerFactory {

    val stages: List<Duration>

    fun calibrate()
}

internal fun TimerState.update(stages: List<Duration>) {
    currentStage = stages.getStage(0)
    currentRemaining = if (currentStage.isIndefinite)
        Duration.ZERO
    else
        currentStage
    nextStage = stages.getStage(1)
    totalTime = stages.sum()
}

