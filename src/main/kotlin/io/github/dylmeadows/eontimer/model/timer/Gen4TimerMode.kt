package io.github.dylmeadows.eontimer.model.timer

import io.github.dylmeadows.common.javafx.util.Choice

enum class Gen4TimerMode(private val text: String) : Choice {
    STANDARD("Standard");

    override fun getText(): String {
        return text
    }
}