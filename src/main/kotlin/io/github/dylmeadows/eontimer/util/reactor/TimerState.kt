package io.github.dylmeadows.eontimer.util.reactor

import java.time.Duration

data class TimerState(
    val elapsed: Duration = Duration.ZERO,
    val duration: Duration = Duration.ZERO) {
    val remaining: Duration get() = duration - elapsed
}