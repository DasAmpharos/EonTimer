package io.github.dylmeadows.eontimer.model.resource

import io.github.dylmeadows.common.core.io.Resource
import javafx.scene.text.Font

enum class FontResource(private val path: String) : Resource {
    ROBOTO_REGULAR("Roboto-Regular.ttf"),
    ROBOTO_CONDENSED("RobotoCondensed-Regular.ttf");

    override fun getPath(): String {
        return "$BASE_RESOURCE_PATH/fonts/${this.path}"
    }

    fun load() {
        Font.loadFont(FontResource::class.java.classLoader.getResource(getPath()).toExternalForm(), 0.0)
    }
}