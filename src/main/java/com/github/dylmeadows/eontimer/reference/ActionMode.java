package com.github.dylmeadows.eontimer.reference;

import com.github.dylmeadows.common.util.LocalizedOption;
import com.github.dylmeadows.common.util.ResourceBundles;

public enum ActionMode implements LocalizedOption {
    AUDIO, VISUAL, AV, NONE;

    public static ActionMode findByLocalizedValue(String i18n) {
        return LocalizedOption.findByLocalizedValue(ActionMode.class, i18n);
    }

    @Override
    public String getLocalizedValue() {
        return ResourceBundles.getBundle(ActionMode.class).getString(name());
    }

    @Override
    public String toString() {
        return getLocalizedValue();
    }
}
