package io.github.dylmeadows.eontimer.util

import io.github.dylmeadows.eontimer.model.settings.Console

object Calibrations {

    fun convertToMillis(delays: Int, console: Console): Long =
        Math.round(delays * console.frameRate)

    fun convertToDelays(millis: Int, console: Console): Long =
        Math.round(millis / console.frameRate)

    fun createCalibration(delay: Int, second: Int, console: Console): Long {
        val delayCalibration = delay - convertToDelays(second * 1000, console).toInt()
        return convertToMillis(delayCalibration, console)
    }
}