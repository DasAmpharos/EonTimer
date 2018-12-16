package com.github.dylmeadows.eontimer.controller.timer;

import com.github.dylmeadows.eontimer.model.timer.CustomTimerModel;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import moe.tristan.easyfxml.api.FxmlController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SuppressWarnings({"unused", "WeakerAccess"})
public class CustomTimerController implements FxmlController {

    private final CustomTimerModel model;

    @Override
    public void initialize() {

    }
}
