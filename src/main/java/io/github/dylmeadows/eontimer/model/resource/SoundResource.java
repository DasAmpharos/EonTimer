package io.github.dylmeadows.eontimer.model.resource;

import io.github.dylmeadows.common.core.io.Resource;
import io.github.dylmeadows.common.javafx.util.Choice;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SoundResource implements Resource, Choice {
    BEEP("Beep", "io/github/dylmeadows/eontimer/sounds/beep.wav"),
    TICK("Tick", "io/github/dylmeadows/eontimer/sounds/tick.wav"),
    DING("Ding", "io/github/dylmeadows/eontimer/sounds/ding.wav"),
    POP("Pop", "io/github/dylmeadows/eontimer/sounds/pop.wav");

    private final String text;
    private final String path;
}
