package com.github.dylmeadows.eontimer.model.resource;

import com.github.dylmeadows.java.io.Resource;
import com.github.dylmeadows.javafx.util.Option;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SoundResource implements Resource, Option {
    BEEP("Beep", "com/github/dylmeadows/eontimer/sounds/beep.wav"),
    TICK("Tick", "com/github/dylmeadows/eontimer/sounds/tick.wav"),
    DING("Ding", "com/github/dylmeadows/eontimer/sounds/ding.wav"),
    POP("Pop", "com/github/dylmeadows/eontimer/sounds/pop.wav");

    private final String text;
    private final String path;
}
