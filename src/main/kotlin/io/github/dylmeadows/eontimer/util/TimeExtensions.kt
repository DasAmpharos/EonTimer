package io.github.dylmeadows.eontimer.util

import io.github.dylmeadows.eontimer.model.settings.TimerSettingsConstants
import java.time.Duration
import java.time.Instant

val now: Long get() = System.currentTimeMillis()
val INDEFINITE = Duration.between(Instant.MIN, Instant.MAX)!!
inline val Duration.isIndefinite: Boolean get() = this == INDEFINITE

inline val Long.minutes: Duration get() = Duration.ofMinutes(this)
inline val Long.seconds: Duration get() = Duration.ofSeconds(this)
inline val Long.milliseconds: Duration get() = Duration.ofMillis(this)
inline val Long.nanoseconds: Duration get() = Duration.ofNanos(this)

fun Long.normalize(): Long {
    var normalized = this
    while (normalized < TimerSettingsConstants.MINIMUM_LENGTH)
        normalized += 1L.minutes.toMillis()
    return normalized
}
