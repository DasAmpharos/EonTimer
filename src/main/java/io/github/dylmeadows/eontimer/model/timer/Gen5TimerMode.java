package io.github.dylmeadows.eontimer.model.timer;

import io.github.dylmeadows.common.javafx.util.Choice;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gen5TimerMode implements Choice {
    STANDARD("Standard"),
    C_GEAR("C-Gear"),
    ENTRALINK("Entralink"),
    ENHANCED_ENTRALINK("Entralink+");

    private final String text;
}
