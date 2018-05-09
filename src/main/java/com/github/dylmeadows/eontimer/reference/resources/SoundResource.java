package com.github.dylmeadows.eontimer.reference.resources;

import com.github.dylmeadows.common.util.LocalizedOption;
import com.github.dylmeadows.common.util.Resource;
import com.github.dylmeadows.common.util.ResourceBundles;

import java.io.BufferedInputStream;
import java.io.InputStream;

public enum SoundResource implements Resource, LocalizedOption {
    BEEP,
    DING,
    POP,
    TICK;

    private final String key;

    private final String path;

    SoundResource() {
        key = Resource.getKey(this);
        path = ResourceMap.hasKey(key) ? ResourceMap.getString(key) : null;
    }

    public static SoundResource findByLocalizedValue(String i18n) {
        return LocalizedOption.findByLocalizedValue(SoundResource.class, i18n);
    }

    @Override
    public InputStream getStream() {
        InputStream stream = SoundResource.class.getClassLoader().getResourceAsStream(path);
        return new BufferedInputStream(stream);
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getKey() {
        return key;
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
