package io.github.dylmeadows.eontimer.model.timer

object TimerConstants {
    @JvmField
    val DEFAULT_TIMER_TYPE = TimerType.GEN5
    const val UPDATE_FACTOR = 1.0
    const val CLOSE_UPDATE_FACTOR = 0.75
    const val CLOSE_THRESHOLD = 167
    const val NULL_TIME_SPAN = -999L
    const val NULL_TIME_SPAN_TEXT = "0:00"
    const val INFINITE_TIME_SPAN = -99L
    const val INFINITE_TIME_SPAN_TEXT = "?:??"
}