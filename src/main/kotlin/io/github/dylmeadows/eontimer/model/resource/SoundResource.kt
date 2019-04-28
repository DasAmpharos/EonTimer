package io.github.dylmeadows.eontimer.model.resource

import io.github.dylmeadows.common.core.io.Resource
import io.github.dylmeadows.common.javafx.util.Choice

enum class SoundResource(private val path: String) : Resource, Choice {
    BEEP("beep.wav"),
    TICK("tick.wav"),
    DING("ding.wav"),
    POP("pop.wav");

    override fun getPath(): String {
        return "$BASE_RESOURCE_PATH/sounds/${this.path}"
    }

    override fun getText(): String {
        return name.toLowerCase().capitalize()
    }
}