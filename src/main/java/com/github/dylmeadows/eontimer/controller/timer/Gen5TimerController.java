package com.github.dylmeadows.eontimer.controller.timer;

import com.github.dylmeadows.eontimer.model.timer.Gen5TimerModel;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import moe.tristan.easyfxml.api.FxmlController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SuppressWarnings({"unused", "WeakerAccess"})
public class Gen5TimerController implements FxmlController {

    private final Gen5TimerModel model;

    @Override
    public void initialize() {

    }
}
