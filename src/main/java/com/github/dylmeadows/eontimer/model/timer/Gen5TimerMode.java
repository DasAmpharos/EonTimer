package com.github.dylmeadows.eontimer.model.timer;

import com.github.dylmeadows.javafx.util.Option;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gen5TimerMode implements Option {
    STANDARD("Standard"),
    C_GEAR("C-Gear"),
    ENTRALINK("Entralink"),
    ENHANCED_ENTRALINK("Entralink+");

    private final String text;
}
