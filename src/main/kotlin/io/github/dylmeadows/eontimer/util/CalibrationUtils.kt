package io.github.dylmeadows.eontimer.util

import io.github.dylmeadows.eontimer.model.settings.Console

fun convertToMillis(delays: Int, console: Console): Int =
    Math.round(delays * console.frameRate).toInt()

fun convertToDelays(millis: Int, console: Console): Int =
    Math.round(millis / console.frameRate).toInt()

fun createCalibration(delay: Int, second: Int, console: Console): Int {
    val delayCalibration = delay - convertToDelays(second * 1000, console)
    return convertToMillis(delayCalibration, console)
}