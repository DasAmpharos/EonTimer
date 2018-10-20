package com.github.dylmeadows.eontimer.model.resources;

import com.github.dylmeadows.common.util.Resource;

public enum ImageResource implements Resource {
    DEFAULT_BACKGROUND_IMAGE("default_background_image.png");

    private final String path;

    ImageResource(String path) {
        this.path = path;
    }

    @Override
    public String getPath() {
        return path;
    }
}
