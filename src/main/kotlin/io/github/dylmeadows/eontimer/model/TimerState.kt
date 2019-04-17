package io.github.dylmeadows.eontimer.model

import java.time.Duration

data class TimerState(
    val elapsed: Duration,
    val totalDuration: Duration,
    val currentStage: StageState,
    val nextStage: StageState)

data class StageState(
    val elapsed: Duration,
    val totalDuration: Duration)
