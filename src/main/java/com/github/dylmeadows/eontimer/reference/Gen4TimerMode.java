package com.github.dylmeadows.eontimer.reference;

import com.github.dylmeadows.common.util.LocalizedOption;
import com.github.dylmeadows.common.util.ResourceBundles;

public enum Gen4TimerMode implements LocalizedOption {
    STANDARD;

    public static Gen4TimerMode findByLocalizedValue(String i18n) {
        return LocalizedOption.findByLocalizedValue(Gen4TimerMode.class, i18n);
    }

    @Override
    public String getLocalizedValue() {
        return ResourceBundles.getBundle(Gen4TimerMode.class).getString(name());
    }

    @Override
    public String toString() {
        return getLocalizedValue();
    }
}
