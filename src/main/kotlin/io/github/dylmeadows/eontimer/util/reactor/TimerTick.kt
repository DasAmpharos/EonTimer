package io.github.dylmeadows.eontimer.util.reactor

import java.time.Duration

data class TimerTick(
    val delta: Duration = Duration.ZERO,
    val elapsed: Duration = Duration.ZERO,
    val duration: Duration = Duration.ZERO) {
    val remaining: Duration get() = duration - elapsed
}