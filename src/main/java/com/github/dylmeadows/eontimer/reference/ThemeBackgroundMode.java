package com.github.dylmeadows.eontimer.reference;

import com.github.dylmeadows.common.util.LocalizedOption;
import com.github.dylmeadows.common.util.ResourceBundles;

public enum ThemeBackgroundMode implements LocalizedOption {
    DEFAULT, COLOR, IMAGE;

    public static ThemeBackgroundMode findByLocalizedValue(String i18n) {
        return LocalizedOption.findByLocalizedValue(ThemeBackgroundMode.class, i18n);
    }

    @Override
    public String getLocalizedValue() {
        return ResourceBundles.getBundle(ThemeBackgroundMode.class).getString(name());
    }

    @Override
    public String toString() {
        return getLocalizedValue();
    }
}
