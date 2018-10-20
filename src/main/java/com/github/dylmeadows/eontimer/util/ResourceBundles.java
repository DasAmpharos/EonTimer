package com.github.dylmeadows.eontimer.util;

import lombok.experimental.UtilityClass;

import java.util.ResourceBundle;

@UtilityClass
public class ResourceBundles {

    public ResourceBundle getBundle(Class<?> clazz) {
        return ResourceBundle.getBundle(clazz.getName());
    }
}
