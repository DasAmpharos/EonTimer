package io.github.dylmeadows.eontimer.model.settings

import io.github.dylmeadows.common.javafx.util.Choice

enum class Console(private val text: String, val fps: Double) : Choice {
    GBA("GBA", 59.7271),
    NDS("NDS", 59.8261),
    DSI("DSI", 59.8261),
    _3DS("3DS", 59.8261);

    val frameRate: Double get() = 1000 / fps

    override fun getText(): String {
        return this.text
    }
}