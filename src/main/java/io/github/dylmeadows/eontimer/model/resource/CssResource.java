package io.github.dylmeadows.eontimer.model.resource;

import io.github.dylmeadows.common.core.io.Resource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CssResource implements Resource {
    EON_TIMER("io/github/dylmeadows/eontimer/css/EonTimer.css");

    private final String path;
}
