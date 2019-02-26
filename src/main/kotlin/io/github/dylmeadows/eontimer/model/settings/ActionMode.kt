package io.github.dylmeadows.eontimer.model.settings

import io.github.dylmeadows.common.javafx.util.Choice
import io.github.dylmeadows.eontimer.util.properName

enum class ActionMode : Choice {
    AUDIO, VISUAL, AV, NONE;

    override fun getText(): String {
        return properName
    }
}