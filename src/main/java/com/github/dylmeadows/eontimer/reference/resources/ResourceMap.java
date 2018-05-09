package com.github.dylmeadows.eontimer.reference.resources;

import com.github.dylmeadows.common.util.ResourceBundles;

import java.util.ResourceBundle;

class ResourceMap {

    static boolean hasKey(String key) {
        return getBundle().containsKey(key);
    }

    static String getString(String key) {
        return hasKey(key) ? getBundle().getString(key) : null;
    }

    private static ResourceBundle getBundle() {
        return ResourceBundles.getBundle(ResourceMap.class);
    }

}
