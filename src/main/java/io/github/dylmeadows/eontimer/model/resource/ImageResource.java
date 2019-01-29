package io.github.dylmeadows.eontimer.model.resource;

import io.github.dylmeadows.common.core.io.Resource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImageResource implements Resource {
    DEFAULT_BACKGROUND_IMAGE("default_background_image.png");

    private final String path;
}
