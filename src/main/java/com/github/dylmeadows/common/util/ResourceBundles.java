package com.github.dylmeadows.common.util;

import java.util.ResourceBundle;

public class ResourceBundles {

    private ResourceBundles() {
    }

    public static ResourceBundle getBundle(Class<?> clazz) {
        return ResourceBundle.getBundle(clazz.getName());
    }
}
