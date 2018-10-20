package com.github.dylmeadows.eontimer.util;

import lombok.experimental.UtilityClass;

import java.util.ResourceBundle;

@UtilityClass
public class Extensions {
    public String shout(String it) {
        return it.toUpperCase() + "!";
    }

    public ResourceBundle getResourceBundle(Class<ResourceBundle> $class, Class<?> className) {
        return ResourceBundle.getBundle(className.getName());
    }
}
