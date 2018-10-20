package com.github.dylmeadows.eontimer.model.resources;

import com.github.dylmeadows.common.util.Resource;

public enum CssResource implements Resource {
    EON_TIMER("com/github/dylmeadows/eontimer/css/EonTimer.css");

    private final String path;

    CssResource(String path) {
        this.path = path;
    }

    @Override
    public String getPath() {
        return path;
    }
}
