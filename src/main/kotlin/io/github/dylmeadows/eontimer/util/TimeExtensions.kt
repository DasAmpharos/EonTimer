package io.github.dylmeadows.eontimer.util

import io.github.dylmeadows.eontimer.model.settings.TimerSettingsConstants
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit

val INDEFINITE = Duration.between(Instant.MIN, Instant.MAX)!!
inline val Duration.isIndefinite: Boolean get() = this == INDEFINITE

inline val Long.minutes: Duration get() = Duration.ofMinutes(this)
inline val Long.seconds: Duration get() = Duration.ofSeconds(this)
inline val Long.milliseconds: Duration get() = Duration.ofMillis(this)
inline val Long.nanoseconds: Duration get() = Duration.ofNanos(this)

fun Duration.toSeconds(): Long {
    return get(ChronoUnit.SECONDS)
}

fun List<Duration>.sum(): Duration {
    return if (!contains(INDEFINITE)) {
        Duration.ofMillis(map(Duration::toMillis).sum())
    } else {
        INDEFINITE
    }
}

fun Long.toMinimumLength(): Long {
    var normalized = this
    while (normalized < TimerSettingsConstants.MINIMUM_LENGTH)
        normalized += 1L.minutes.toMillis()
    return normalized
}

fun List<Duration>.getStage(index: Int): Duration {
    return if (index < size) get(index) else Duration.ZERO
}
