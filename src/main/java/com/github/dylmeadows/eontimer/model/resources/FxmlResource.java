package com.github.dylmeadows.eontimer.model.resources;

import com.github.dylmeadows.common.util.Resource;

public enum FxmlResource implements Resource {
    GEN_3_TIMER_VIEW("com/github/dylmeadows/eontimer/fxml/Gen3TimerView.fxml");

    private final String path;

    FxmlResource(String path) {
        this.path = path;
    }

    @Override
    public String getPath() {
        return path;
    }
}
