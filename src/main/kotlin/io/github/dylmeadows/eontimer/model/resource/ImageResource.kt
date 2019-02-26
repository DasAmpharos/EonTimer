package io.github.dylmeadows.eontimer.model.resource

import io.github.dylmeadows.common.core.io.Resource

enum class ImageResource(private val path: String) : Resource {
    DefaultBackgroundImage("$BASE_RESOURCE_PATH/img/default_background_image.png");

    override fun getPath(): String {
        return this.path
    }
}