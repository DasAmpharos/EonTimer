package io.github.dylmeadows.eontimer.controller.timer;

import io.github.dylmeadows.eontimer.model.timer.CustomTimerModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CustomTimerController {

    private final CustomTimerModel model;
}
