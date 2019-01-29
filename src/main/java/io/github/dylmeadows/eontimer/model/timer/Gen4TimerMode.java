package io.github.dylmeadows.eontimer.model.timer;

import io.github.dylmeadows.common.javafx.util.Choice;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gen4TimerMode implements Choice {
    STANDARD("Standard");

    private final String text;
}
