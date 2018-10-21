package com.github.dylmeadows.eontimer.model.resources;

import com.github.dylmeadows.eontimer.model.Option;
import com.github.dylmeadows.common.util.Resource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SoundResource implements Resource, Option {
    BEEP("Beep", "com/github/dylmeadows/eontimer/model/resources/sounds/beep.wav"),
    TICK("Tick", "com/github/dylmeadows/eontimer/model/resources/sounds/tick.wav"),
    DING("Ding", "com/github/dylmeadows/eontimer/model/resources/sounds/ding.wav"),
    POP("Pop", "com/github/dylmeadows/eontimer/model/resources/sounds/pop.wav");

    private final String text;
    private final String path;
}
