package com.github.dylmeadows.common.javafx.node;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;

public class GridPaneLayout {

    private Map<Node, NodeConstraints> constraintsMap;
    private Set<Position> positionSet;
    
    private static final String GET_ERROR = "attempting to get %s for a node that has not been added";
    private static final String SET_ERROR = "attempting to set %s for a node that has not been added";

    public GridPaneLayout() {
        constraintsMap = new LinkedHashMap<>();
        positionSet = new HashSet<>();
    }

    public void apply(GridPane gridPane) {
        gridPane.getChildren().clear();
        constraintsMap.forEach((node, constraints) -> {
            GridPane.setConstraints(node, constraints.row(), constraints.column());
            GridPane.setColumnSpan(node, constraints.columnSpan());
            GridPane.setRowSpan(node, constraints.rowSpan());

            Optional.ofNullable(constraints.hAlignment())
                    .ifPresent(hAlignment -> GridPane.setHalignment(node, hAlignment));
            Optional.ofNullable(constraints.vAlignment())
                    .ifPresent(vAlignment -> GridPane.setValignment(node, vAlignment));
            Optional.ofNullable(constraints.hGrow())
                    .ifPresent(hGrow -> GridPane.setHgrow(node, hGrow));
            Optional.ofNullable(constraints.vGrow())
                    .ifPresent(vGrow -> GridPane.setVgrow(node, vGrow));
            Optional.ofNullable(constraints.margin())
                    .ifPresent(margin -> GridPane.setMargin(node, margin));

            gridPane.getChildren().add(node);
        });
    }

    public void add(Node node, int column, int row) {
        addImpl(node, column, row, 1, 1, null, null, null, null, null);
    }

    public void add(Node node, int column, int row, int columnSpan, int rowSpan) {
        addImpl(node, column, row, columnSpan, rowSpan, null, null, null, null, null);
    }

    public void add(Node node, int column, int row, int columnSpan, int rowSpan, HPos hAlignment, VPos vAlignment) {
        addImpl(node, column, row, columnSpan, rowSpan, hAlignment, vAlignment, null, null, null);
    }

    public void add(Node node, int column, int row, int columnSpan, int rowSpan, HPos hAlignment, VPos vAlignment, Priority hGrow, Priority vGrow) {
        addImpl(node, column, row, columnSpan, rowSpan, hAlignment, vAlignment, hGrow, vGrow, null);
    }

    public void add(Node node, int column, int row, int columnSpan, int rowSpan, HPos hAlignment, VPos vAlignment, Priority hGrow, Priority vGrow, Insets margin) {
        addImpl(node, column, row, columnSpan, rowSpan, hAlignment, vAlignment, hGrow, vGrow, margin);
    }

    private void addImpl(Node node, int column, int row, int columnSpan, int rowSpan, HPos hAlignment, VPos vAlignment, Priority hGrow, Priority vGrow, Insets margin) {
        Position pos = new Position(row, column);
        checkArgument(!contains(node), "attempting to add a node that has already been added to the layout");
        checkArgument(!positionSet.contains(pos), "attempting to add a node where another node has already been added");

        constraintsMap.put(node, new NodeConstraints()
                .row(row)
                .rowSpan(rowSpan)
                .column(column)
                .columnSpan(columnSpan)
                .hAlignment(hAlignment)
                .vAlignment(vAlignment)
                .hGrow(hGrow)
                .vGrow(vGrow)
                .margin(margin));
        positionSet.add(pos);
    }

    public void remove(Node node) {
        checkArgument(contains(node), "attempting to remove a node that has not been added");
        NodeConstraints constraints = constraintsMap.remove(node);
        positionSet.remove(constraints.position());
    }

    public boolean contains(Node node) {
        return Optional.ofNullable(node)
                .filter(constraintsMap::containsKey)
                .isPresent();
    }

    public void move(Node node, int row, int column) {
        checkArgument(contains(node), "attempting to move a node that has not been added");
        setRow(node, row);
        setColumn(node, column);
    }

    public int getRow(Node node) {
        checkArgument(contains(node), GET_ERROR, "row");
        return constraintsMap.get(node).row();
    }

    public void setRow(Node node, int row) {
        checkArgument(contains(node), SET_ERROR, "row");
        NodeConstraints constraints = constraintsMap.get(node);
        Position currentPos = constraints.position();
        Position newPos = currentPos.withRow(row);
        checkArgument(!positionSet.contains(newPos), "attempting to move a node where another node is currently set");

        constraints.row(row);
        positionSet.remove(currentPos);
        positionSet.add(newPos);
    }

    public int getColumn(Node node) {
        checkArgument(contains(node), GET_ERROR, "column");
        return constraintsMap.get(node).column();
    }

    public void setColumn(Node node, int column) {
        checkArgument(contains(node), SET_ERROR, "column");
        NodeConstraints constraints = constraintsMap.get(node);
        Position currentPos = constraints.position();
        Position newPos = currentPos.withColumn(column);
        checkArgument(!positionSet.contains(newPos), "attempting to move a node where another node is currently set");

        constraints.column(column);
        positionSet.remove(currentPos);
        positionSet.add(newPos);
    }

    public int getRowSpan(Node node) {
        checkArgument(contains(node), GET_ERROR, "rowSpan");
        return constraintsMap.get(node).rowSpan();
    }

    public void setRowSpan(Node node, int rowSpan) {
        checkArgument(contains(node), SET_ERROR, "rowSpan");
        constraintsMap.get(node).rowSpan(rowSpan);
    }

    public int getColumnSpan(Node node) {
        checkArgument(contains(node), GET_ERROR, "columnSpan");
        return constraintsMap.get(node).columnSpan();
    }

    public void setColumnSpan(Node node, int columnSpan) {
        checkArgument(contains(node), SET_ERROR, "columnSpan");
        constraintsMap.get(node).columnSpan(columnSpan);
    }

    public void setSpan(Node node, int columnSpan, int rowSpan) {
        checkArgument(contains(node), SET_ERROR, "span");
        setColumnSpan(node, columnSpan);
        setRowSpan(node, rowSpan);
    }

    public HPos getHAlignment(Node node) {
        checkArgument(contains(node), GET_ERROR, "hAlignment");
        return constraintsMap.get(node).hAlignment();
    }

    public void setHAlignment(Node node, HPos hAlignment) {
        checkArgument(contains(node), SET_ERROR, "hAlignment");
        constraintsMap.get(node).hAlignment(hAlignment);
    }

    public VPos getVAlignment(Node node) {
        checkArgument(contains(node), GET_ERROR, "vAlignment");
        return constraintsMap.get(node).vAlignment();
    }

    public void setVAlignment(Node node, VPos vAlignment) {
        checkArgument(contains(node), SET_ERROR, "vAlignment");
        constraintsMap.get(node).vAlignment(vAlignment);
    }

    public void setAlignment(Node node, HPos hAlignment, VPos vAlignment) {
        checkArgument(contains(node), SET_ERROR, "alignment");
        setHAlignment(node, hAlignment);
        setVAlignment(node, vAlignment);
    }

    public Priority getHGrow(Node node) {
        checkArgument(contains(node), GET_ERROR, "hGrow");
        return constraintsMap.get(node).hGrow();
    }

    public void setHGrow(Node node, Priority hGrow) {
        checkArgument(contains(node), SET_ERROR, "hGrow");
        constraintsMap.get(node).hGrow(hGrow);
    }

    public Priority getVGrow(Node node) {
        checkArgument(contains(node), GET_ERROR, "vGrow");
        return constraintsMap.get(node).vGrow();
    }

    public void setVGrow(Node node, Priority vGrow) {
        checkArgument(contains(node), SET_ERROR, "vGrow");
        constraintsMap.get(node).vGrow(vGrow);
    }

    public void setGrow(Node node, Priority hGrow, Priority vGrow) {
        checkArgument(contains(node), SET_ERROR, "grow");
        setHGrow(node, hGrow);
        setVGrow(node, vGrow);
    }

    public Insets getMargin(Node node) {
        checkArgument(contains(node), GET_ERROR, "margin");
        return constraintsMap.get(node).margin();
    }

    public void setMargin(Node node, Insets margin) {
        checkArgument(contains(node), SET_ERROR, "margin");
        constraintsMap.get(node).margin(margin);
    }
}
