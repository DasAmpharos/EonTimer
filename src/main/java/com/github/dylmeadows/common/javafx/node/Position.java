package com.github.dylmeadows.common.javafx.node;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.Wither;

@Data
@Wither
@Accessors(fluent = true)
class Position {
    private final int row;
    private final int column;
}
