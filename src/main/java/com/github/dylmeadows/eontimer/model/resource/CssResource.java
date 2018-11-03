package com.github.dylmeadows.eontimer.model.resource;

import com.github.dylmeadows.java.io.Resource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CssResource implements Resource {
    EON_TIMER("com/github/dylmeadows/eontimer/css/EonTimer.css");

    private final String path;
}
