package io.github.dylmeadows.eontimer.util.reactor

data class TimerTick(val delta: Long, val elapsed: Long) {
    companion object {
        val ZERO = TimerTick(0L, 0L)
    }

    operator fun plus(delta: Long): TimerTick {
        return TimerTick(delta, elapsed + delta)
    }
}