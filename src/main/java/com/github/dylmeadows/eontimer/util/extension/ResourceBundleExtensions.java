package com.github.dylmeadows.eontimer.util.extension;

import lombok.experimental.UtilityClass;

import java.util.ResourceBundle;

@UtilityClass
public class ResourceBundleExtensions {

    public ResourceBundle getBundle(Class<?> clazz) {
        return ResourceBundle.getBundle(clazz.getName());
    }
}
