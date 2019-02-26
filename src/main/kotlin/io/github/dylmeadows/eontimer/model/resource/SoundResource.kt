package io.github.dylmeadows.eontimer.model.resource

import io.github.dylmeadows.common.core.io.Resource
import io.github.dylmeadows.common.javafx.util.Choice

enum class SoundResource(private val path: String) : Resource, Choice {
    BEEP("$BASE_RESOURCE_PATH/sounds/beep.wav"),
    TICK("$BASE_RESOURCE_PATH/sounds/tick.wav"),
    DING("$BASE_RESOURCE_PATH/sounds/ding.wav"),
    POP("$BASE_RESOURCE_PATH/sounds/pop.wav");

    override fun getPath(): String {
        return this.path
    }

    override fun getText(): String {
        return name.toLowerCase().capitalize()
    }
}