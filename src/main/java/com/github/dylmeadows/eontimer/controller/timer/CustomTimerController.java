package com.github.dylmeadows.eontimer.controller.timer;

import com.github.dylmeadows.eontimer.model.timer.CustomTimerModel;
import moe.tristan.easyfxml.api.FxmlController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings({"unused", "WeakerAccess"})
public class CustomTimerController implements FxmlController {

    private final CustomTimerModel model;

    @Autowired
    public CustomTimerController(final CustomTimerModel model) {
        this.model = model;
    }

    @Override
    public void initialize() {

    }
}
