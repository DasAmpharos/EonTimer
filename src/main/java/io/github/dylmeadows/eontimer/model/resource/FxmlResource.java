package io.github.dylmeadows.eontimer.model.resource;

import io.github.dylmeadows.common.core.io.Resource;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum FxmlResource implements Resource {
    Gen3TimerPane("io/github/dylmeadows/eontimer/fxml/timer/Gen3TimerPane.fxml"),
    Gen4TimerPane("io/github/dylmeadows/eontimer/fxml/timer/Gen4TimerPane.fxml"),
    Gen5TimerPane("io/github/dylmeadows/eontimer/fxml/timer/Gen5TimerPane.fxml"),
    TimerDisplayPane("io/github/dylmeadows/eontimer/fxml/TimerDisplayPane.fxml"),
    TimerMasterPane("io/github/dylmeadows/eontimer/fxml/TimerMasterPane.fxml");

    private final String path;

}
