package io.github.dylmeadows.eontimer.util.reactor

import java.time.Duration

data class TimerTick(val delta: Duration, val elapsed: Duration) {
    companion object {
        val ZERO = TimerTick(Duration.ZERO, Duration.ZERO)
    }

    operator fun plus(delta: Duration): TimerTick {
        return TimerTick(delta, elapsed + delta)
    }
}