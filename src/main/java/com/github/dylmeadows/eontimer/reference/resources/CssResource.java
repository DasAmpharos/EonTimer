package com.github.dylmeadows.eontimer.reference.resources;

import com.github.dylmeadows.common.util.Resource;

public enum CssResource implements Resource {
    EON_TIMER;

    private final String key;

    private final String path;

    CssResource() {
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
