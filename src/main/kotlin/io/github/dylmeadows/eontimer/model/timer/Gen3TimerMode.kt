package io.github.dylmeadows.eontimer.model.timer

import io.github.dylmeadows.common.javafx.util.Choice

enum class Gen3TimerMode(private val text: String) : Choice {
    STANDARD("Standard"),
    VARIABLE_TARGET("Variable Target");

    override fun getText(): String {
        return text
    }
}