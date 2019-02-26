package io.github.dylmeadows.eontimer.model

data class TimerState(
    val minutesBeforeTarget: Int,
    val currentStage: Long,
    val remaining: Long,
    val nextStage: Long)