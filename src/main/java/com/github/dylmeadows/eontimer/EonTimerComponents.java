package com.github.dylmeadows.eontimer;

import com.github.dylmeadows.eontimer.controller.TimerDisplayController;
import com.github.dylmeadows.eontimer.controller.timer.CustomTimerController;
import com.github.dylmeadows.eontimer.controller.timer.Gen3TimerController;
import com.github.dylmeadows.eontimer.controller.timer.Gen4TimerController;
import com.github.dylmeadows.eontimer.controller.timer.Gen5TimerController;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import moe.tristan.easyfxml.api.FxmlController;
import moe.tristan.easyfxml.api.FxmlFile;
import moe.tristan.easyfxml.api.FxmlNode;

@Getter
@RequiredArgsConstructor
public enum EonTimerComponents implements FxmlNode {
    GEN3_TIMER("timer/Gen3TimerPane.fxml", Gen3TimerController.class),
    GEN4_TIMER("timer/Gen4TimerPane.fxml", Gen4TimerController.class),
    GEN5_TIMER("timer/Gen5TimerPane.fxml", Gen5TimerController.class),
    CUSTOM_TIMER("timer/CustomTimerPane.fxml", CustomTimerController.class),
    TIMER_DISPLAY("TimerDisplayPane.fxml", TimerDisplayController.class);

    private final String path;
    private final Class<? extends FxmlController> controllerClass;

    private static final String BASE_PATH = "com/github/dylmeadows/eontimer/fxml";

    @Override
    public FxmlFile getFile() {
        return () -> BASE_PATH + "/" + path;
    }
}
