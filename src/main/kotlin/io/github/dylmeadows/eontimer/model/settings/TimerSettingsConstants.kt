@file:JvmName("TimerSettingsConstants")

package io.github.dylmeadows.eontimer.model.settings

object TimerSettingsConstants {
    @JvmField
    val DEFAULT_CONSOLE = Console.NDS

    const val DEFAULT_PRECISION_CALIBRATION_MODE = false
    const val DEFAULT_REFRESH_INTERVAL = 8L
    const val MINIMUM_LENGTH = 14000
}