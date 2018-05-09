package com.github.dylmeadows.common.util;

public interface LocalizedOption {

    static <T extends Enum<T> & LocalizedOption> T findByLocalizedValue(Class<T> clazz, String i18n) {
        for (T option : clazz.getEnumConstants()) {
            if (option.getLocalizedValue().equals(i18n)) {
                return option;
            }
        }
        return null;
    }

    String getLocalizedValue();
}
