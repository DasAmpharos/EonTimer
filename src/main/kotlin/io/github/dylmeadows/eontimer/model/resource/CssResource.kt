package io.github.dylmeadows.eontimer.model.resource

import io.github.dylmeadows.common.core.io.Resource

enum class CssResource(private val path: String) : Resource {
    MAIN("main.css");

    override fun getPath(): String {
        return "$BASE_RESOURCE_PATH/css/${this.path}"
    }
}