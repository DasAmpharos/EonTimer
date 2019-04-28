package io.github.dylmeadows.eontimer.model.timer

import io.github.dylmeadows.common.javafx.util.Choice

enum class Gen5TimerMode(private val text: String) : Choice {
    STANDARD("Standard"),
    C_GEAR("C-Gear"),
    ENTRALINK("Entralink"),
    ENHANCED_ENTRALINK("Entralink+");

    override fun getText(): String {
        return text
    }
}