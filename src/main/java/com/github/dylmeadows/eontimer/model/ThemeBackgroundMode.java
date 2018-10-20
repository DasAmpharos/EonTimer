package com.github.dylmeadows.eontimer.model;

import com.github.dylmeadows.common.util.LocalizedEnum;
import com.github.dylmeadows.eontimer.util.ResourceBundles;

public enum ThemeBackgroundMode implements LocalizedEnum {
    DEFAULT, COLOR, IMAGE;

    @Override
    public String getLocalizedValue() {
        return ResourceBundles.getBundle(ThemeBackgroundMode.class).getString(name());
    }

    @Override
    public String toString() {
        return getLocalizedValue();
    }
}
