package io.github.dylmeadows.eontimer.model.config;

import io.github.dylmeadows.common.javafx.util.Choice;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActionMode implements Choice {
    AUDIO("Audio"),
    VISUAL("Visual"),
    AV("A/V"),
    NONE("None");

    private final String text;
}
