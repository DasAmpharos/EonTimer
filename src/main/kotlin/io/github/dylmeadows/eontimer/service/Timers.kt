package io.github.dylmeadows.eontimer.service

import io.github.dylmeadows.eontimer.util.INDEFINITE
import io.github.dylmeadows.eontimer.util.reactor.TimerTick
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.Instant

object Timers {

    suspend fun fixedTimer(period: Duration, duration: Duration, onTick: (TimerTick) -> Unit): Duration {
        return fixedTimer(period, duration, Duration.ZERO, onTick)
    }

    suspend fun fixedTimer(period: Duration, duration: Duration, preElapsed: Duration, onTick: (TimerTick) -> Unit): Duration {
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

            onTick(TimerTick(delta, elapsed, duration))
        }
        return elapsed
    }

    suspend fun variableTimer(period: Duration, preElapsed: Duration, onTick: (TimerTick) -> Unit, takeUntil: (Duration) -> Boolean): Duration {
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

            onTick(TimerTick(delta, elapsed, INDEFINITE))
        }
        return elapsed
    }
}