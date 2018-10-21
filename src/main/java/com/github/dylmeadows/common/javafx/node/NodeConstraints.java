package com.github.dylmeadows.common.javafx.node;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.layout.Priority;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
class NodeConstraints {
    private int row;
    private int rowSpan = 1;
    private int column;
    private int columnSpan = 1;
    private HPos hAlignment;
    private VPos vAlignment;
    private Priority hGrow;
    private Priority vGrow;
    private Insets margin;

    public Position position() {
        return new Position(row, column);
    }

    public NodeConstraints position(Position position) {
        return row(position.row()).column(position.column());
    }
}