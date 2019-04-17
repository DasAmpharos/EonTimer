package io.github.dylmeadows.eontimer.service

import kotlinx.coroutines.delay
import java.time.Duration
import java.time.Instant

object Timers {

    suspend fun fixedTimer(period: Duration, duration: Duration, onTick: (Duration) -> Unit): Duration {
        return fixedTimer(period, duration, Duration.ZERO, onTick)
    }

    suspend fun fixedTimer(period: Duration, duration: Duration, preElapsed: Duration, onTick: (Duration) -> Unit): Duration {
        var elapsed = preElapsed
        var adjustedDelay = period
        var lastTimestamp = Instant.now()
        while (elapsed < duration) {
            delay(adjustedDelay.toMillis())

            val now = Instant.now()
            val delta = Duration.between(lastTimestamp, now)
            if ((duration - elapsed) < period) {
                adjustedDelay = duration - elapsed
            } else {
                adjustedDelay -= delta - period
            }
            lastTimestamp = now
            elapsed += delta
            onTick(elapsed)
        }
        return elapsed
    }

    suspend fun variableTimer(period: Duration, preElapsed: Duration, onTick: (Duration) -> Unit, takeUntil: (Duration) -> Boolean): Duration {
        var elapsed = preElapsed
        var adjustedDelay = period
        var lastTimestamp = Instant.now()
        while (!takeUntil(elapsed)) {
            delay(adjustedDelay.toMillis())

            val now = Instant.now()
            val delta = Duration.between(lastTimestamp, now)
            adjustedDelay -= delta - period
            lastTimestamp = now
            elapsed += delta
            onTick(elapsed)
        }
        return elapsed
    }
}