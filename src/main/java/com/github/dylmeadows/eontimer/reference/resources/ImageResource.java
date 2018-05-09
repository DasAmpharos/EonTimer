package com.github.dylmeadows.eontimer.reference.resources;

import com.github.dylmeadows.common.util.Resource;

public enum ImageResource implements Resource {
    DEFAULT_BACKGROUND_IMAGE;

    private final String key;

    private final String path;

    ImageResource() {
        key = Resource.getKey(this);
        path = ResourceMap.hasKey(key) ? ResourceMap.getString(key) : null;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getKey() {
        return key;
    }
}
