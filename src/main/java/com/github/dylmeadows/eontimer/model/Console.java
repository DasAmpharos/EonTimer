package com.github.dylmeadows.eontimer.model;

import lombok.Getter;

@Getter
public enum Console implements Option {
    GBA("GBA", 59.7271),
    NDS("NDS", 59.8261),
    DSI("DSI", 59.8261),
    _3DS("3DS", 59.8261);

    private final String text;
    private final double fps;
    private final double frameRate;

    Console(String text, double fps) {
        this.text = text;
        this.fps = fps;
        this.frameRate = 1000 / fps;
    }
}
