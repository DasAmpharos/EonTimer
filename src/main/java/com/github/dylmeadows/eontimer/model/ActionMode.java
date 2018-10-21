package com.github.dylmeadows.eontimer.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActionMode implements Option {
    AUDIO("Audio"),
    VISUAL("Visual"),
    AV("A/V"),
    NONE("None");

    private final String text;
}
