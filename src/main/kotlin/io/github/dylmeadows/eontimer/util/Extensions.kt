package io.github.dylmeadows.eontimer.util

import io.github.dylmeadows.eontimer.model.settings.TimerSettingsConstants
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val <T : Any> T.log: Logger
    get() = LoggerFactory.getLogger(javaClass)

fun Long.normalize(): Long {
    var normalized = this
    while (normalized < TimerSettingsConstants.MINIMUM_LENGTH)
        normalized += 60000
    return normalized
}