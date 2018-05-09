package com.github.dylmeadows.common.javafx.node;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.util.LinkedHashMap;

public class GridPaneLayout {

    private LinkedHashMap<Node, int[]> nodeMap;
    private NodeConstraints[][] layout;

    private static final int ROW = 0, COLUMN = 1;

    private static final String GET_ERROR = "attempting to get a layout property for a node that has not been added";
    private static final String SET_ERROR = "attempting to set a layout property for a node that has not been added";

    public GridPaneLayout(int columns, int rows) {
        nodeMap = new LinkedHashMap<>();
        layout = new NodeConstraints[rows][columns];
    }

    public void apply(GridPane gridPane) {
        gridPane.getChildren().clear();
        for (Node node : nodeMap.keySet()) {
            int[] pos = nodeMap.get(node);
            GridPane.setConstraints(node, pos[COLUMN], pos[ROW]);
            NodeConstraints constraints = layout[pos[ROW]][pos[COLUMN]];
            GridPane.setColumnSpan(node, constraints.columnSpan);
            GridPane.setRowSpan(node, constraints.rowSpan);
            if (constraints.hAlignment != null)
                GridPane.setHalignment(node, constraints.hAlignment);
            if (constraints.vAlignment != null)
                GridPane.setValignment(node, constraints.vAlignment);
            if (constraints.hGrow != null)
                GridPane.setHgrow(node, constraints.hGrow);
            if (constraints.vGrow != null)
                GridPane.setVgrow(node, constraints.vGrow);
            if (constraints.margin != null)
                GridPane.setMargin(node, constraints.margin);
            gridPane.getChildren().add(node);
        }
    }

    public void add(Node node, int column, int row) {
        if (contains(node))
            throw new IllegalArgumentException("attempting to add a node that is already in layout");
        if (layout[row][column] != null)
            throw new IllegalArgumentException("attempting to add a node where another node is set (row=" + row + "; column=" + column + ")");

        nodeMap.put(node, new int[]{row, column});
        layout[row][column] = new NodeConstraints();
    }

    public void add(Node node, int column, int row, int columnSpan, int rowSpan) {
        add(node, column, row, columnSpan, rowSpan, null, null);
    }

    public void add(Node node, int column, int row, int columnSpan, int rowSpan, HPos hAlignment, VPos vAlignment) {
        add(node, column, row, columnSpan, rowSpan, hAlignment, vAlignment, null, null);
    }

    public void add(Node node, int column, int row, int columnSpan, int rowSpan, HPos hAlignment, VPos vAlignment, Priority hGrow, Priority vGrow) {
        add(node, column, row, columnSpan, rowSpan, hAlignment, vAlignment, hGrow, vGrow, null);
    }

    public void add(Node node, int column, int row, int columnSpan, int rowSpan, HPos hAlignment, VPos vAlignment, Priority hGrow, Priority vGrow, Insets margin) {
        if (contains(node))
            throw new IllegalArgumentException("attempting to add a node that is already in layout");
        if (layout[row][column] != null)
            throw new IllegalArgumentException("attempting to add a node where another node is set (row=" + row + "; column=" + column + ")");

        nodeMap.put(node, new int[]{row, column});
        NodeConstraints constraints = new NodeConstraints();
        constraints.columnSpan = columnSpan;
        constraints.rowSpan = rowSpan;
        constraints.hAlignment = hAlignment;
        constraints.vAlignment = vAlignment;
        constraints.hGrow = hGrow;
        constraints.vGrow = vGrow;
        constraints.margin = margin;
        layout[row][column] = constraints;
    }

    public void remove(Node node) {
        if (contains(node)) {
            int[] pos = nodeMap.get(node);
            layout[pos[ROW]][pos[COLUMN]] = null;
            nodeMap.remove(node);
        }
    }

    public boolean contains(Node node) {
        return nodeMap.containsKey(node);
    }

    public void move(Node node, int row, int column) {
        setRow(node, row);
        setColumn(node, column);
    }

    public int getRow(Node node) {
        if (!contains(node))
            throw new IllegalArgumentException(GET_ERROR);
        return nodeMap.get(node)[ROW];
    }

    public void setRow(Node node, int row) {
        if (!contains(node))
            throw new IllegalArgumentException(SET_ERROR);
        int[] pos = nodeMap.get(node);
        if (layout[row][pos[COLUMN]] != null) {
            throw new IllegalArgumentException("attempting to move a node where another node is set (row=" + row + "; column=" + pos[COLUMN] + ")");
        }
        NodeConstraints constraints = layout[pos[ROW]][pos[COLUMN]];
        layout[row][pos[COLUMN]] = constraints;
        layout[pos[ROW]][pos[COLUMN]] = null;
    }

    public int getColumn(Node node) {
        if (!contains(node))
            throw new IllegalArgumentException(GET_ERROR);
        return nodeMap.get(node)[COLUMN];
    }

    public void setColumn(Node node, int column) {
        if (!contains(node))
            throw new IllegalArgumentException(SET_ERROR);
        int[] pos = nodeMap.get(node);
        if (layout[pos[ROW]][column] != null) {
            throw new IllegalArgumentException("attempting to move a node where another node is set (row=" + pos[ROW] + "; column=" + column + ")");
        }
        NodeConstraints constraints = layout[pos[ROW]][pos[COLUMN]];
        layout[pos[ROW]][column] = constraints;
        layout[pos[ROW]][pos[COLUMN]] = null;
    }

    public int getRowSpan(Node node) {
        if (!contains(node))
            throw new IllegalArgumentException(GET_ERROR);
        return getConstraints(node).rowSpan;
    }

    public void setRowSpan(Node node, int rowSpan) {
        if (!contains(node))
            throw new IllegalArgumentException(SET_ERROR);
        getConstraints(node).rowSpan = rowSpan;
    }

    public int getColumnSpan(Node node) {
        if (!contains(node))
            throw new IllegalArgumentException(GET_ERROR);
        return getConstraints(node).columnSpan;
    }

    public void setColumnSpan(Node node, int columnSpan) {
        if (!contains(node))
            throw new IllegalArgumentException(SET_ERROR);
        getConstraints(node).columnSpan = columnSpan;
    }

    public void setSpans(Node node, int columnSpan, int rowSpan) {
        setColumnSpan(node, columnSpan);
        setRowSpan(node, rowSpan);
    }

    public HPos getHAlignment(Node node) {
        if (!contains(node))
            throw new IllegalArgumentException(GET_ERROR);
        return getConstraints(node).hAlignment;
    }

    public void setHAlignment(Node node, HPos hAlignment) {
        if (!contains(node))
            throw new IllegalArgumentException(SET_ERROR);
        getConstraints(node).hAlignment = hAlignment;
    }

    public VPos getVAlignment(Node node) {
        if (!contains(node))
            throw new IllegalArgumentException(GET_ERROR);
        return getConstraints(node).vAlignment;
    }

    public void setVAlignment(Node node, VPos vAlignment) {
        if (!contains(node))
            throw new IllegalArgumentException(SET_ERROR);
        getConstraints(node).vAlignment = vAlignment;
    }

    public void setAlignment(Node node, HPos hAlignment, VPos vAlignment) {
        setHAlignment(node, hAlignment);
        setVAlignment(node, vAlignment);
    }

    public Priority getHGrow(Node node) {
        if (!contains(node))
            throw new IllegalArgumentException(GET_ERROR);
        return getConstraints(node).hGrow;
    }

    public void setHGrow(Node node, Priority hGrow) {
        if (!contains(node))
            throw new IllegalArgumentException(SET_ERROR);
        getConstraints(node).hGrow = hGrow;
    }

    public Priority getVGrow(Node node) {
        if (!contains(node))
            throw new IllegalArgumentException(GET_ERROR);
        return getConstraints(node).vGrow;
    }

    public void setVGrow(Node node, Priority vGrow) {
        if (!contains(node))
            throw new IllegalArgumentException(SET_ERROR);
        getConstraints(node).vGrow = vGrow;
    }

    public void setGrow(Node node, Priority hGrow, Priority vGrow) {
        setHGrow(node, hGrow);
        setVGrow(node, vGrow);
    }

    public Insets getMargin(Node node) {
        if (!contains(node))
            throw new IllegalArgumentException(GET_ERROR);
        return getConstraints(node).margin;
    }

    public void setMargin(Node node, Insets margin) {
        if (!contains(node))
            throw new IllegalArgumentException(SET_ERROR);
        getConstraints(node).margin = margin;
    }

    private NodeConstraints getConstraints(Node node) {
        if (!contains(node))
            throw new IllegalArgumentException("attempting to get constraints for a node that has not been added");
        int[] pos = nodeMap.get(node);
        return layout[pos[ROW]][pos[COLUMN]];
    }

    class NodeConstraints {
        int rowSpan = 1;
        int columnSpan = 1;
        HPos hAlignment;
        VPos vAlignment;
        Priority hGrow, vGrow;
        Insets margin;
    }
}
