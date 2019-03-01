package io.github.dylmeadows.eontimer.model.settings

import io.github.dylmeadows.common.javafx.util.Choice

enum class ActionMode(private val text: String) : Choice {
    AUDIO("Audio"),
    VISUAL("Visual"),
    AV("A/V"),
    NONE("None");

    override fun getText(): String {
        return text
    }
}