package com.github.dylmeadows.eontimer.component.timer;

import com.github.dylmeadows.eontimer.model.Gen5TimerModel;
import moe.tristan.easyfxml.api.FxmlController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings({"unused", "WeakerAccess"})
public class Gen5TimerController implements FxmlController {

    private final Gen5TimerModel model;

    @Autowired
    public Gen5TimerController(final Gen5TimerModel model) {
        this.model = model;
    }

    @Override
    public void initialize() {

    }
}
