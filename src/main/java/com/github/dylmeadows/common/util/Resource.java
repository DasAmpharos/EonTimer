package com.github.dylmeadows.common.util;

import java.io.InputStream;
import java.net.URL;

public interface Resource {

    static <T extends Enum<T>> String getKey(T resource) {
        return String.format("%s.%s", resource.getClass().getSimpleName(), resource.name());
    }

    default URL getUrl() {
        return Resource.class.getClassLoader().getResource(getPath());
    }

    default InputStream getStream() {
        return Resource.class.getClassLoader().getResourceAsStream(getPath());
    }

    default boolean isDefined() {
        return getPath() != null && !getPath().isEmpty();
    }

    String getPath();

    String getKey();
}
