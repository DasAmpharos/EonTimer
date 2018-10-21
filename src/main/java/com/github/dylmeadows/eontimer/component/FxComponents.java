package com.github.dylmeadows.eontimer.component;

import com.github.dylmeadows.eontimer.component.timer.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import moe.tristan.easyfxml.api.FxmlController;
import moe.tristan.easyfxml.api.FxmlFile;
import moe.tristan.easyfxml.api.FxmlNode;

@Getter
@RequiredArgsConstructor
public enum FxComponents implements FxmlNode {
    GEN3("timer/Gen3TimerPane.fxml", Gen3TimerController.class);

    private final String path;
    private final Class<? extends FxmlController> controllerClass;

    private static final String BASE_PATH = "com/github/dylmeadows/eontimer/component";

    @Override
    public FxmlFile getFile() {
        return () -> BASE_PATH + "/" + path;
    }
}
