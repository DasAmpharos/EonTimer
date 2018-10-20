package com.github.dylmeadows.eontimer.model.resources;

import com.github.dylmeadows.common.util.LocalizedEnum;
import com.github.dylmeadows.common.util.Resource;
import com.github.dylmeadows.eontimer.util.ResourceBundles;

public enum SoundResource implements Resource, LocalizedEnum {
    BEEP("beep.wav"),
    TICK("tick.wav"),
    DING("ding.wav"),
    POP("pop.wav");

    private final String path;

    SoundResource(String path) {
        this.path = path;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getLocalizedValue() {
        return ResourceBundles.getBundle(SoundResource.class).getString(name());
    }

    @Override
    public String toString() {
        return getLocalizedValue();
    }
}
