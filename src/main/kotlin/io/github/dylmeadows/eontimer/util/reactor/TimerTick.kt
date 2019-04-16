package io.github.dylmeadows.eontimer.util.reactor

import java.time.Duration

data class TimerTick(val delta: Duration, val elapsed: Duration)