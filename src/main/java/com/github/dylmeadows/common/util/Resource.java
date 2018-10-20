package com.github.dylmeadows.common.util;

import java.io.InputStream;
import java.net.URL;

public interface Resource {

    default URL get() {
        return Resource.class.getClassLoader().getResource(getPath());
    }

    default InputStream getAsStream() {
        return Resource.class.getClassLoader().getResourceAsStream(getPath());
    }

    String getPath();
}
