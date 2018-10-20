package com.github.dylmeadows.eontimer.model;

import com.github.dylmeadows.common.util.LocalizedEnum;
import com.github.dylmeadows.eontimer.util.ResourceBundles;

public enum Console implements LocalizedEnum {
    GBA(59.7271),
    NDS(59.8261),
    DSI(59.8261),
    _3DS(59.8261);

    private final double fps;
    private final double frameRate;

    Console(double fps) {
        this.fps = fps;
        this.frameRate = 1000 / fps;
    }

    @Override
    public String getLocalizedValue() {
        return ResourceBundles.getBundle(Console.class).getString(name());
    }

    @Override
    public String toString() {
        return getLocalizedValue();
    }

    public double getFps() {
        return fps;
    }

    public double getFrameRate() {
        return frameRate;
    }
}
